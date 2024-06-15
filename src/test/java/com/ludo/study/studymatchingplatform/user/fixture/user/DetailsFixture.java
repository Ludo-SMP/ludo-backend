package com.ludo.study.studymatchingplatform.user.fixture.user;

import com.ludo.study.studymatchingplatform.user.domain.user.Details;
import com.ludo.study.studymatchingplatform.user.domain.user.User;

public class DetailsFixture {

	public static Details createDetails(final User user) {
		return Details.builder()
				.user(user)
				.finishStudy(1)
				.perfectStudy(1)
				.accumulatedTeamMembers(1)
				.existingDayOfAttendance(1.1)
				.existingMandatoryDayOfAttendance(1.1)
				.activeness(1)
				.professionalism(1)
				.communication(1)
				.together(1)
				.recommend(1)
				.build();
	}

}
