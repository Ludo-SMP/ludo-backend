import { AxiosError, HttpStatusCode } from "axios";
import { subDays } from "date-fns";
import { describe } from "node:test";
import { ApiClient } from "../config/api-client";
import { fakeSignupBody } from "../fixtures/auth-fixture";
import { randPositionId } from "../fixtures/position-fixture";
import { fakeWriteRecruitmentRequest } from "../fixtures/recruitment-fixture";
import { fakeWriteReviewRequest } from "../fixtures/review-fixture";
import { fakeCreateStudyRequest } from "../fixtures/study-fixture";
import { login, signup } from "../helpers/auth-api-helper";
import { utcNow } from "../helpers/datetime-helper";
import {
  applyRecruitment,
  writeRecruitment,
} from "../helpers/recruitment-api-helper";
import { getPeerReviews, writeReview } from "../helpers/review-api-helper";
import { acceptApplicant, createStudy } from "../helpers/study-api-helper";
import { updateStudyEndDateTime } from "../helpers/study-db-helper";
import { BaseResponse } from "../types/base-types";

describe("리뷰 작성", () => {
  test("[200 OK] 스터디 종료 이후 14일 이내 리뷰 작성 가능", async () => {
    // given
    const client = ApiClient.default();
    const owner = fakeSignupBody();
    await signup(client, owner);
    const {
      data: {
        data: {
          study: { id: studyId },
        },
      },
    } = await createStudy(client, fakeCreateStudyRequest({}));
    const {
      data: {
        data: { recruitment },
      },
    } = await writeRecruitment(client, studyId, fakeWriteRecruitmentRequest());

    const applicantUser = fakeSignupBody();
    const {
      data: {
        data: {
          user: { id: applicantUserId },
        },
      },
    } = await signup(client, applicantUser);
    await applyRecruitment(client, studyId, recruitment.id, {
      positionId: randPositionId(),
    });

    // login to owner
    await login(client, owner);
    await acceptApplicant(client, studyId, applicantUserId);
    await updateStudyEndDateTime(subDays(utcNow(), 10));

    // when
    const request = fakeWriteReviewRequest({
      revieweeId: applicantUserId,
    });
    const {
      status,
      data: {
        data: { review },
      },
    } = await writeReview(client, studyId, request);

    // // then
    expect(status).toBe(HttpStatusCode.Created);
    expect(review.reviewer.email).toBe(owner.email);
    expect(review.reviewee.email).toBe(applicantUser.email);
  });

  test("[200 OK] 자신이 작성한 review 스터디 전체 review 확인 시 조회 가능", async () => {
    // given
    const client = ApiClient.default();
    const owner = fakeSignupBody();
    const {
      data: {
        data: {
          user: { id: ownerId },
        },
      },
    } = await signup(client, owner);
    const {
      data: {
        data: {
          study: { id: studyId },
        },
      },
    } = await createStudy(client, fakeCreateStudyRequest({}));
    const {
      data: {
        data: { recruitment },
      },
    } = await writeRecruitment(client, studyId, fakeWriteRecruitmentRequest());

    const applicantUser = fakeSignupBody();
    const {
      data: {
        data: {
          user: { id: applicantUserId },
        },
      },
    } = await signup(client, applicantUser);
    await applyRecruitment(client, studyId, recruitment.id, {
      positionId: randPositionId(),
    });

    // login to owner
    await login(client, owner);
    await acceptApplicant(client, studyId, applicantUserId);
    await updateStudyEndDateTime(subDays(utcNow(), 10));

    // when
    const request = fakeWriteReviewRequest({
      revieweeId: applicantUserId,
    });
    await writeReview(client, studyId, request);

    const {
      status,
      data: {
        data: { reviews },
      },
    } = await getPeerReviews(client, studyId);

    // then
    expect(status).toBe(HttpStatusCode.Ok);
    expect(reviews.length).toBeGreaterThan(0);
    expect(reviews[0].selfReview.activenessScore).toEqual(
      request.activenessScore
    );
    expect(reviews[0].selfReview.communicationScore).toEqual(
      request.communicationScore
    );
    expect(reviews[0].selfReview.professionalismScore).toEqual(
      request.professionalismScore
    );
    expect(reviews[0].selfReview.recommendScore).toEqual(
      request.recommendScore
    );
    expect(reviews[0].selfReview.togetherScore).toEqual(request.togetherScore);
    expect(reviews[0].selfReview.reviewerId).toEqual(ownerId);
    expect(reviews[0].selfReview.revieweeId).toEqual(request.revieweeId);
    expect(reviews[0].peerReview).toBeNull();
  });

  test("[200 OK] 상대방이 작성한 review 스터디 전체 review 확인 시 조회 가능 조회 가능", async () => {
    // given
    const client = ApiClient.default();
    const owner = fakeSignupBody();
    const {
      data: {
        data: {
          user: { id: ownerId },
        },
      },
    } = await signup(client, owner);
    const {
      data: {
        data: {
          study: { id: studyId },
        },
      },
    } = await createStudy(client, fakeCreateStudyRequest({}));
    const {
      data: {
        data: { recruitment },
      },
    } = await writeRecruitment(client, studyId, fakeWriteRecruitmentRequest());

    const applicantUser = fakeSignupBody();
    const {
      data: {
        data: {
          user: { id: applicantUserId },
        },
      },
    } = await signup(client, applicantUser);
    await applyRecruitment(client, studyId, recruitment.id, {
      positionId: randPositionId(),
    });

    // login to owner
    await login(client, owner);
    await acceptApplicant(client, studyId, applicantUserId);
    await updateStudyEndDateTime(subDays(utcNow(), 10));

    await login(client, applicantUser);

    // when
    const request = fakeWriteReviewRequest({
      revieweeId: ownerId,
    });
    await writeReview(client, studyId, request);

    // login to owner
    await login(client, owner);

    const {
      status,
      data: {
        data: { reviews },
      },
    } = await getPeerReviews(client, studyId);

    // then
    expect(status).toBe(HttpStatusCode.Ok);
    expect(reviews.length).toBeGreaterThan(0);
    expect(reviews[0].peerReview.activenessScore).toEqual(
      request.activenessScore
    );
    expect(reviews[0].peerReview.communicationScore).toEqual(
      request.communicationScore
    );
    expect(reviews[0].peerReview.professionalismScore).toEqual(
      request.professionalismScore
    );
    expect(reviews[0].peerReview.recommendScore).toEqual(
      request.recommendScore
    );
    expect(reviews[0].peerReview.togetherScore).toEqual(request.togetherScore);
    expect(reviews[0].peerReview.reviewerId).toEqual(applicantUserId);
    expect(reviews[0].peerReview.revieweeId).toEqual(request.revieweeId);
    expect(reviews[0].selfReview).toBeNull();
  });

  test("[400 BAD_REQUEST] 스터디가 종료 전에는 리뷰 작성 불가", async () => {
    // given
    const client = ApiClient.default();
    const owner = fakeSignupBody();
    await signup(client, owner);
    const {
      data: {
        data: {
          study: { id: studyId },
        },
      },
    } = await createStudy(client, fakeCreateStudyRequest());
    const {
      data: {
        data: { recruitment },
      },
    } = await writeRecruitment(client, studyId, fakeWriteRecruitmentRequest());

    const applicantUser = fakeSignupBody();
    const {
      data: {
        data: {
          user: { id: applicantUser1Id },
        },
      },
    } = await signup(client, applicantUser);
    await applyRecruitment(client, studyId, recruitment.id, {
      positionId: randPositionId(),
    });

    // login to owner
    await login(client, owner);
    await acceptApplicant(client, studyId, applicantUser1Id);

    // when
    const request = fakeWriteReviewRequest({
      revieweeId: applicantUser1Id,
    });

    // then
    try {
      await writeReview(client, studyId, request);
    } catch (err) {
      const e = err as AxiosError<BaseResponse<undefined>>;

      expect(e.response?.status).toBe(HttpStatusCode.BadRequest);
      expect(e.response?.data.message).toBeDefined();
      expect(e.response?.data.data).toBeNull();
    }
  });

  test("[400 BAD_REQUEST] 스터디 종료 이후 14일 이후 리뷰 작성 불가", async () => {
    // given
    const client = ApiClient.default();
    const owner = fakeSignupBody();
    await signup(client, owner);
    const {
      data: {
        data: {
          study: { id: studyId },
        },
      },
    } = await createStudy(client, fakeCreateStudyRequest({}));
    const {
      data: {
        data: { recruitment },
      },
    } = await writeRecruitment(client, studyId, fakeWriteRecruitmentRequest());

    const applicantUser = fakeSignupBody();
    const {
      data: {
        data: {
          user: { id: applicantUser1Id },
        },
      },
    } = await signup(client, applicantUser);
    await applyRecruitment(client, studyId, recruitment.id, {
      positionId: randPositionId(),
    });

    // login to owner
    await login(client, owner);
    await acceptApplicant(client, studyId, applicantUser1Id);

    // end study for enabling to write review
    await updateStudyEndDateTime(subDays(utcNow(), 20));

    // when
    const request = fakeWriteReviewRequest({
      revieweeId: applicantUser1Id,
    });

    // then
    try {
      await writeReview(client, studyId, request);
    } catch (err) {
      const e = err as AxiosError<BaseResponse<undefined>>;

      expect(e.response?.status).toBe(HttpStatusCode.BadRequest);
      expect(e.response?.data.message).toEqual(
        "리뷰 작성 기간이 지났습니다. 리뷰 작성은 스터디 완료 후 최대 14일까지 가능합니다."
      );
      expect(e.response?.data.data).toBeNull();
    }
  });

  test("[400 BAD_REQUEST] 자기 자신에게 리뷰 작성 불가", async () => {
    // given
    const client = ApiClient.default();
    const owner = fakeSignupBody();
    const {
      data: {
        data: {
          user: { id: ownerId },
        },
      },
    } = await signup(client, owner);
    const {
      data: {
        data: {
          study: { id: studyId },
        },
      },
    } = await createStudy(client, fakeCreateStudyRequest({}));
    const {
      data: {
        data: { recruitment },
      },
    } = await writeRecruitment(client, studyId, fakeWriteRecruitmentRequest());

    const applicantUser = fakeSignupBody();
    const {
      data: {
        data: {
          user: { id: applicantUser1Id },
        },
      },
    } = await signup(client, applicantUser);
    await applyRecruitment(client, studyId, recruitment.id, {
      positionId: randPositionId(),
    });

    // login to owner
    await login(client, owner);
    await acceptApplicant(client, studyId, applicantUser1Id);

    // end study for enabling to write review
    await updateStudyEndDateTime(subDays(utcNow(), 10));

    // when
    const request = fakeWriteReviewRequest({
      revieweeId: ownerId,
    });

    // then
    try {
      await writeReview(client, studyId, request);
    } catch (err) {
      const e = err as AxiosError<BaseResponse<undefined>>;

      expect(e.response?.status).toBe(HttpStatusCode.BadRequest);
      expect(e.response?.data.message).toEqual(
        "자기 자신에게는 리뷰를 작성할 수 없습니다."
      );
      expect(e.response?.data.data).toBeNull();
    }
  });

  test("[403 FORBIDDEN] 스터디원에 참여 중이지 않은 사용자에 대한 리뷰 작성 불가", async () => {
    // given
    const client = ApiClient.default();
    const owner = fakeSignupBody();
    const {
      data: {
        data: {
          user: { id: ownerId },
        },
      },
    } = await signup(client, owner);
    const {
      data: {
        data: {
          study: { id: studyId },
        },
      },
    } = await createStudy(client, fakeCreateStudyRequest({}));

    const {
      data: {
        data: {
          user: { id: guestId },
        },
      },
    } = await signup(client, fakeSignupBody());

    // end study for enabling to write review
    await updateStudyEndDateTime(subDays(utcNow(), 10));

    // when
    const request = fakeWriteReviewRequest({
      revieweeId: ownerId,
    });

    // then
    try {
      await writeReview(client, studyId, request);
    } catch (err) {
      const e = err as AxiosError<BaseResponse<undefined>>;

      expect(e.response?.status).toBe(HttpStatusCode.Forbidden);
      expect(e.response?.data.message).toEqual(
        "참여 중인 스터디가 아닙니다. 리뷰를 작성할 수 없습니다."
      );
      expect(e.response?.data.data).toBeNull();
    }
  });

  test("[403 FORBIDDEN] 스터디원이 아니면 리뷰 작성 불가", async () => {
    // given
    const client = ApiClient.default();
    const owner = fakeSignupBody();
    const {
      data: {
        data: {
          user: { id: ownerId },
        },
      },
    } = await signup(client, owner);
    const {
      data: {
        data: {
          study: { id: studyId },
        },
      },
    } = await createStudy(client, fakeCreateStudyRequest({}));

    const {
      data: {
        data: {
          user: { id: guestId },
        },
      },
    } = await signup(client, fakeSignupBody());

    await updateStudyEndDateTime(subDays(utcNow(), 10));

    // when
    const request = fakeWriteReviewRequest({
      revieweeId: ownerId,
    });

    // then
    try {
      await writeReview(client, studyId, request);
    } catch (err) {
      const e = err as AxiosError<BaseResponse<undefined>>;

      expect(e.response?.status).toBe(HttpStatusCode.Forbidden);
      expect(e.response?.data.message).toEqual(
        "참여 중인 스터디가 아닙니다. 리뷰를 작성할 수 없습니다."
      );
      expect(e.response?.data.data).toBeNull();
    }
  });

  test("[404 NOT_FOUND] 존재하지 않는 스터디는 리뷰 작성 불가", async () => {
    // given
    const client = ApiClient.default();
    const owner = fakeSignupBody();
    const {
      data: {
        data: {
          user: { id: ownerId },
        },
      },
    } = await signup(client, owner);

    // when
    const request = fakeWriteReviewRequest({
      revieweeId: ownerId,
    });

    // then
    try {
      await writeReview(client, 9999999999999, request);
    } catch (err) {
      const e = err as AxiosError<BaseResponse<undefined>>;

      expect(e.response?.status).toBe(HttpStatusCode.NotFound);
      expect(e.response?.data.message).toEqual(
        "존재하지 않는 스터디입니다. 리뷰를 작성할 수 없습니다."
      );
      expect(e.response?.data.data).toBeNull();
    }
  });

  test("[409 CONFLICT] 이미 리뷰를 작성한 스터디원에게 리뷰 재작성 불가", async () => {
    // given
    const client = ApiClient.default();
    const owner = fakeSignupBody();
    const {
      data: {
        data: {
          user: { id: ownerId },
        },
      },
    } = await signup(client, owner);
    const {
      data: {
        data: {
          study: { id: studyId },
        },
      },
    } = await createStudy(client, fakeCreateStudyRequest({}));
    const {
      data: {
        data: { recruitment },
      },
    } = await writeRecruitment(client, studyId, fakeWriteRecruitmentRequest());

    const applicantUser = fakeSignupBody();
    const {
      data: {
        data: {
          user: { id: applicantUser1Id },
        },
      },
    } = await signup(client, applicantUser);
    await applyRecruitment(client, studyId, recruitment.id, {
      positionId: randPositionId(),
    });

    // login to owner
    await login(client, owner);
    await acceptApplicant(client, studyId, applicantUser1Id);

    // end study for enabling to write review
    await updateStudyEndDateTime(subDays(utcNow(), 10));

    // when
    const request = fakeWriteReviewRequest({
      revieweeId: applicantUser1Id,
    });
    await writeReview(client, studyId, request);

    // then
    try {
      await writeReview(client, studyId, request);
    } catch (err) {
      const e = err as AxiosError<BaseResponse<undefined>>;

      expect(e.response?.status).toBe(HttpStatusCode.Conflict);
      expect(e.response?.data.message).toEqual("이미 리뷰를 작성 하셨습니다.");
      expect(e.response?.data.data).toBeNull();
    }
  });
});
