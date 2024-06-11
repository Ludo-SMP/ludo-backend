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
import { applyRecruitment, writeRecruitment } from "../helpers/recruitment-api-helper";
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
        data: { studies },
      },
    } = await getPeerReviews(client);

    const reviews = studies[0].reviews;
    // then
    expect(status).toBe(HttpStatusCode.Ok);
    expect(reviews.length).toBeGreaterThan(0);
    expect(reviews[0].selfReview.activeness).toEqual(
      request.activenessScore,
    );
    expect(reviews[0].selfReview.communication).toEqual(
      request.communicationScore,
    );
    expect(reviews[0].selfReview.professionalism).toEqual(
      request.professionalismScore,
    );
    expect(reviews[0].selfReview.recommend).toEqual(
      request.recommendScore,
    );
    expect(reviews[0].selfReview.together).toEqual(request.togetherScore);
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
        data: { studies },
      },
    } = await getPeerReviews(client);

    const reviews = studies[0].reviews;

    // then
    expect(status).toBe(HttpStatusCode.Ok);
    expect(reviews.length).toBeGreaterThan(0);
    expect(reviews[0].peerReview.activeness).toEqual(
      request.activenessScore,
    );
    expect(reviews[0].peerReview.communication).toEqual(
      request.communicationScore,
    );
    expect(reviews[0].peerReview.professionalism).toEqual(
      request.professionalismScore,
    );
    expect(reviews[0].peerReview.recommend).toEqual(
      request.recommendScore,
    );
    expect(reviews[0].peerReview.together).toEqual(request.togetherScore);
    expect(reviews[0].peerReview.reviewerId).toEqual(applicantUserId);
    expect(reviews[0].peerReview.revieweeId).toEqual(request.revieweeId);
    expect(reviews[0].selfReview).toBeNull();
  });

  test("[200 OK] 여러 스터디의 팀원들에게 남긴 리뷰를 한 번에 조회 가능", async () => {
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

    const owner2 = fakeSignupBody();
    const {
      data: {
        data: {
          user: { id: owner2Id },
        },
      },
    } = await signup(client, owner2);
    const {
      data: {
        data: {
          study: { id: study2Id },
        },
      },
    } = await createStudy(client, fakeCreateStudyRequest({}));
    const {
      data: {
        data: { recruitment: recruitment2 },
      },
    } = await writeRecruitment(client, study2Id, fakeWriteRecruitmentRequest());

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

    await applyRecruitment(client, study2Id, recruitment2.id, {
      positionId: randPositionId(),
    });

    // login to owner
    await login(client, owner);
    await acceptApplicant(client, studyId, applicantUserId);
    await updateStudyEndDateTime(subDays(utcNow(), 10));
    await login(client, owner2);
    await acceptApplicant(client, study2Id, applicantUserId);
    await updateStudyEndDateTime(subDays(utcNow(), 10));

    await login(client, applicantUser);

    // // when
    const prevRequest = fakeWriteReviewRequest({
      revieweeId: ownerId,
    });
    const lastestRequest = fakeWriteReviewRequest({
      revieweeId: owner2Id,
    });
    await writeReview(client, studyId, prevRequest);
    await writeReview(client, study2Id, lastestRequest);

    const {
      status,
      data: {
        data: { studies },
      },
    } = await getPeerReviews(client);

    const latestReviews = studies[0].reviews;
    const prevReviews = studies[1].reviews;

    // // then
    expect(status).toBe(HttpStatusCode.Ok);
    expect(latestReviews.length).toBe(1);
    expect(latestReviews[0].selfReview.activeness).toEqual(
      lastestRequest.activenessScore,
    );
    expect(latestReviews[0].selfReview.communication).toEqual(
      lastestRequest.communicationScore,
    );
    expect(latestReviews[0].selfReview.professionalism).toEqual(
      lastestRequest.professionalismScore,
    );
    expect(latestReviews[0].selfReview.recommend).toEqual(
      lastestRequest.recommendScore,
    );
    expect(latestReviews[0].selfReview.together).toEqual(
      lastestRequest.togetherScore,
    );
    expect(latestReviews[0].selfReview.reviewerId).toEqual(applicantUserId);
    expect(latestReviews[0].selfReview.revieweeId).toEqual(
      lastestRequest.revieweeId,
    );
    expect(latestReviews[0].peerReview).toBeNull();

    expect(prevReviews[0].selfReview.activeness).toEqual(
      prevRequest.activenessScore,
    );
    expect(prevReviews[0].selfReview.communication).toEqual(
      prevRequest.communicationScore,
    );
    expect(prevReviews[0].selfReview.professionalism).toEqual(
      prevRequest.professionalismScore,
    );
    expect(prevReviews[0].selfReview.recommend).toEqual(
      prevRequest.recommendScore,
    );
    expect(prevReviews[0].selfReview.together).toEqual(
      prevRequest.togetherScore,
    );
    expect(prevReviews[0].selfReview.reviewerId).toEqual(applicantUserId);
    expect(prevReviews[0].selfReview.revieweeId).toEqual(
      prevRequest.revieweeId,
    );
    expect(prevReviews[0].peerReview).toBeNull();
  });

  test("[200 OK] 여러 스터디의 팀원들이 내게 남긴 리뷰를 한 번에 조회 가능", async () => {
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

    const owner2 = fakeSignupBody();
    const {
      data: {
        data: {
          user: { id: owner2Id },
        },
      },
    } = await signup(client, owner2);
    const {
      data: {
        data: {
          study: { id: study2Id },
        },
      },
    } = await createStudy(client, fakeCreateStudyRequest({}));
    const {
      data: {
        data: { recruitment: recruitment2 },
      },
    } = await writeRecruitment(client, study2Id, fakeWriteRecruitmentRequest());

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

    await applyRecruitment(client, study2Id, recruitment2.id, {
      positionId: randPositionId(),
    });

    // login to owner
    await login(client, owner);
    await acceptApplicant(client, studyId, applicantUserId);
    await updateStudyEndDateTime(subDays(utcNow(), 10));
    await login(client, owner2);
    await acceptApplicant(client, study2Id, applicantUserId);
    await updateStudyEndDateTime(subDays(utcNow(), 10));

    await login(client, applicantUser);

    // // when
    const prevRequest = fakeWriteReviewRequest({
      revieweeId: applicantUserId,
    });
    const lastestRequest = fakeWriteReviewRequest({
      revieweeId: applicantUserId,
    });

    await login(client, owner);
    await writeReview(client, studyId, prevRequest);
    await login(client, owner2);
    await writeReview(client, study2Id, lastestRequest);

    // me
    await login(client, applicantUser);

    const {
      status,
      data: {
        data: { studies },
      },
    } = await getPeerReviews(client);

    // // Review:

    const latestReview = studies[0].reviews;
    const prevReview = studies[1].reviews;

    // // then

    expect(status).toBe(HttpStatusCode.Ok);
    expect(latestReview.length).toBe(1);
    expect(latestReview[0].peerReview.activeness).toEqual(
      lastestRequest.activenessScore,
    );
    expect(latestReview[0].peerReview.communication).toEqual(
      lastestRequest.communicationScore,
    );
    expect(latestReview[0].peerReview.professionalism).toEqual(
      lastestRequest.professionalismScore,
    );
    expect(latestReview[0].peerReview.recommend).toEqual(
      lastestRequest.recommendScore,
    );
    expect(latestReview[0].peerReview.together).toEqual(
      lastestRequest.togetherScore,
    );
    expect(latestReview[0].peerReview.reviewerId).toEqual(owner2Id);
    expect(latestReview[0].peerReview.revieweeId).toEqual(
      lastestRequest.revieweeId,
    );
    expect(latestReview[0].selfReview).toBeNull();

    expect(prevReview[0].peerReview.activeness).toEqual(
      prevRequest.activenessScore,
    );
    expect(prevReview[0].peerReview.communication).toEqual(
      prevRequest.communicationScore,
    );
    expect(prevReview[0].peerReview.professionalism).toEqual(
      prevRequest.professionalismScore,
    );
    expect(prevReview[0].peerReview.recommend).toEqual(
      prevRequest.recommendScore,
    );
    expect(prevReview[0].peerReview.together).toEqual(
      prevRequest.togetherScore,
    );
    expect(prevReview[0].peerReview.reviewerId).toEqual(ownerId);
    expect(prevReview[0].peerReview.revieweeId).toEqual(prevRequest.revieweeId);
    expect(prevReview[0].selfReview).toBeNull();
  });

  test("[200 OK] 여러 스터디의 팀원들이 내게 남긴 리뷰와 내가 남긴 리뷰를 한 번에 조회 가능", async () => {
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

    const owner2 = fakeSignupBody();
    const {
      data: {
        data: {
          user: { id: owner2Id },
        },
      },
    } = await signup(client, owner2);
    const {
      data: {
        data: {
          study: { id: study2Id },
        },
      },
    } = await createStudy(client, fakeCreateStudyRequest({}));
    const {
      data: {
        data: { recruitment: recruitment2 },
      },
    } = await writeRecruitment(client, study2Id, fakeWriteRecruitmentRequest());

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

    await applyRecruitment(client, study2Id, recruitment2.id, {
      positionId: randPositionId(),
    });

    // login to owner
    await login(client, owner);
    await acceptApplicant(client, studyId, applicantUserId);
    await updateStudyEndDateTime(subDays(utcNow(), 10));
    await login(client, owner2);
    await acceptApplicant(client, study2Id, applicantUserId);
    await updateStudyEndDateTime(subDays(utcNow(), 10));

    await login(client, applicantUser);

    // // when
    const prevPeerRequest = fakeWriteReviewRequest({
      revieweeId: applicantUserId,
    });
    const lastestPeerRequest = fakeWriteReviewRequest({
      revieweeId: applicantUserId,
    });
    const prevMyRequest = fakeWriteReviewRequest({
      revieweeId: ownerId,
    });
    const lastestMyRequest = fakeWriteReviewRequest({
      revieweeId: owner2Id,
    });

    await login(client, owner);
    await writeReview(client, studyId, prevPeerRequest);
    await login(client, owner2);
    await writeReview(client, study2Id, lastestPeerRequest);

    await login(client, applicantUser);
    await writeReview(client, studyId, prevMyRequest);
    await writeReview(client, study2Id, lastestMyRequest);

    // me
    const {
      status,
      data: {
        data: { studies },
      },
    } = await getPeerReviews(client);

    const latestReviews = studies[0].reviews;
    const prevReview = studies[1].reviews;
    const latestPeerReview = latestReviews[0].peerReview;
    const latestMyReview = latestReviews[0].selfReview;
    const prevPeerReview = prevReview[0].peerReview;
    const prevMyReview = prevReview[0].selfReview;

    // // then
    expect(status).toBe(HttpStatusCode.Ok);

    // latest Study
    expect(latestReviews.length).toBe(1);

    // latesty Study peer review
    expect(latestPeerReview.activeness).toEqual(
      lastestPeerRequest.activenessScore,
    );
    expect(latestPeerReview.communication).toEqual(
      lastestPeerRequest.communicationScore,
    );
    expect(latestPeerReview.professionalism).toEqual(
      lastestPeerRequest.professionalismScore,
    );
    expect(latestPeerReview.recommend).toEqual(
      lastestPeerRequest.recommendScore,
    );
    expect(latestPeerReview.together).toEqual(
      lastestPeerRequest.togetherScore,
    );
    expect(latestPeerReview.reviewerId).toEqual(owner2Id);
    expect(latestPeerReview.revieweeId).toEqual(applicantUserId);

    // latest study my review
    expect(latestMyReview.activeness).toEqual(
      lastestMyRequest.activenessScore,
    );
    expect(latestMyReview.communication).toEqual(
      lastestMyRequest.communicationScore,
    );
    expect(latestMyReview.professionalism).toEqual(
      lastestMyRequest.professionalismScore,
    );
    expect(latestMyReview.recommend).toEqual(
      lastestMyRequest.recommendScore,
    );
    expect(latestMyReview.together).toEqual(
      lastestMyRequest.togetherScore,
    );
    expect(latestMyReview.reviewerId).toEqual(applicantUserId);
    expect(latestMyReview.revieweeId).toEqual(owner2Id);

    // prev study peer review
    expect(prevPeerReview.activeness).toEqual(
      prevPeerRequest.activenessScore,
    );
    expect(prevPeerReview.communication).toEqual(
      prevPeerRequest.communicationScore,
    );
    expect(prevPeerReview.professionalism).toEqual(
      prevPeerRequest.professionalismScore,
    );
    expect(prevPeerReview.recommend).toEqual(
      prevPeerRequest.recommendScore,
    );
    expect(prevPeerReview.together).toEqual(prevPeerRequest.togetherScore);
    expect(prevPeerReview.reviewerId).toEqual(ownerId);
    expect(prevPeerReview.revieweeId).toEqual(applicantUserId);

    // prev study my review
    expect(prevMyReview.activeness).toEqual(prevMyRequest.activenessScore);
    expect(prevMyReview.communication).toEqual(
      prevMyRequest.communicationScore,
    );
    expect(prevMyReview.professionalism).toEqual(
      prevMyRequest.professionalismScore,
    );
    expect(prevMyReview.recommend).toEqual(prevMyRequest.recommendScore);
    expect(prevMyReview.together).toEqual(prevMyRequest.togetherScore);
    expect(prevMyReview.reviewerId).toEqual(applicantUserId);
    expect(prevMyReview.revieweeId).toEqual(ownerId);
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
        "리뷰 작성 기간이 지났습니다. 리뷰 작성은 스터디 완료 후 최대 14일까지 가능합니다.",
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
        "자기 자신에게는 리뷰를 작성할 수 없습니다.",
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
        "참여 중인 스터디가 아닙니다. 리뷰를 작성할 수 없습니다.",
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
        "참여 중인 스터디가 아닙니다. 리뷰를 작성할 수 없습니다.",
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
        "존재하지 않는 스터디입니다. 리뷰를 작성할 수 없습니다.",
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
