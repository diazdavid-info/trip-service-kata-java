package org.craftedsw.tripservicekata.trip;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.craftedsw.tripservicekata.exception.UserNotLoggedInException;
import org.craftedsw.tripservicekata.user.User;
import org.craftedsw.tripservicekata.user.UserSession;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

public class TripServiceTest extends Assert {

	private TripService tripService;
	private UserSession mockUserSession;
	private User mockUser;
	private User userLogged;
	private TripDAO mockTripDao;

	@Before
	public void setUp() {
		this.mockUserSession = mock(UserSession.class);
		this.mockUser = mock(User.class);
		this.userLogged = new User();
		this.mockTripDao = mock(TripDAO.class);
		this.tripService = new TripService(this.mockUserSession, this.mockTripDao);
	}

	@Test(expected = UserNotLoggedInException.class)
	public void shouldThrowExceptionWhenUserIsNotLogged() {
		when(this.mockUserSession.getLoggedUser()).thenReturn(null);

		this.tripService.getTripsByUser(new User());
	}

	@Test
	public void whenUserIsLoggedAndNotHasFriendThenTheListIsEmpty() {
		when(this.mockUserSession.getLoggedUser()).thenReturn(new User());

		List<Trip> list = this.tripService.getTripsByUser(new User());

		Assert.assertEquals(0, list.size());
	}

	@Test
	public void whenUserIsLoggedHasFriendAndHaveNotTripTogetherThenTheListIsEmpty() {
		List<User> listFriend = getListFriendsWithUserLogger();

		when(this.mockUser.getFriends()).thenReturn(listFriend);
		when(this.mockUser.isMyFriend(this.userLogged)).thenReturn(false);

		when(this.mockUserSession.getLoggedUser()).thenReturn(this.userLogged);

		when(this.mockTripDao.findTripsUser(this.mockUser)).thenReturn(new ArrayList<Trip>());

		List<Trip> list = this.tripService.getTripsByUser(this.mockUser);

		Assert.assertEquals(0, list.size());
	}

	@Test
	public void whenUserIsLoggedHasFriendAndHaveTripsTogetherThenTheListHasTrips() {
		List<User> listFriend = getListFriendsWithUserLogger();
		List<Trip> listTrips = getListTrips();

		when(this.mockUser.getFriends()).thenReturn(listFriend);
		when(this.mockUser.isMyFriend(this.userLogged)).thenReturn(true);

		when(this.mockUserSession.getLoggedUser()).thenReturn(this.userLogged);

		when(this.mockTripDao.findTripsUser(this.mockUser)).thenReturn(listTrips);

		List<Trip> list = this.tripService.getTripsByUser(this.mockUser);

		Assert.assertEquals(2, list.size());
	}

	/**
	 * @return ArrayList<Trip>
	 */
	private ArrayList<Trip> getListTrips() {
		ArrayList<Trip> listTrips = new ArrayList<Trip>();
		listTrips.add(new Trip());
		listTrips.add(new Trip());
		return listTrips;
	}

	/**
	 * @return List<User>
	 */
	private List<User> getListFriendsWithUserLogger() {
		List<User> listFriend = new ArrayList<User>();
		listFriend.add(this.userLogged);
		return listFriend;
	}
}
