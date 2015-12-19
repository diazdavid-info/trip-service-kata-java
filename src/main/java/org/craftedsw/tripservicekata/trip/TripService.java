package org.craftedsw.tripservicekata.trip;

import java.util.ArrayList;
import java.util.List;

import org.craftedsw.tripservicekata.exception.UserNotLoggedInException;
import org.craftedsw.tripservicekata.user.User;
import org.craftedsw.tripservicekata.user.UserSession;

public class TripService {

	private UserSession userSession;
	private TripDAO tripDAO;

	public TripService(UserSession userSession, TripDAO tripDAO) {
		this.userSession = userSession;
		this.tripDAO = tripDAO;
	}

	public List<Trip> getTripsByUser(User user) throws UserNotLoggedInException {
		isLogged();
		return getListTrips(user);
	}

	private List<Trip> getListTrips(User user) {
		List<Trip> tripList = new ArrayList<Trip>();
		if(user.isMyFriend(userSession.getLoggedUser())) {
			tripList = this.tripDAO.findTripsUser(user);
		}
		return tripList;
	}

	/**
	 * @param User
	 *            loggedUser
	 */
	private void isLogged() {
		if (userSession.getLoggedUser() == null) {
			throw new UserNotLoggedInException();
		}
	}

}
