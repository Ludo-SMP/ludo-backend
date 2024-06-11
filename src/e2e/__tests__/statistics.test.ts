import { HttpStatusCode } from "axios";
import { subDays } from "date-fns";
import dns from "node:dns";
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
import { getMyReviewStatistics, getMyStudyStatistics } from "../helpers/statistics-api-helper";
import { acceptApplicant, createStudy } from "../helpers/study-api-helper";
import { updateStudyEndDateTime } from "../helpers/study-db-helper";
dns.setDefaultResultOrder("ipv4first");


describe("statistics Api", () => {
  it("[200 OK] 사용자 스터디 통계 조회 가능", async () => {
    // given
    const client = ApiClient.default();
    await signup(client, fakeSignupBody());

    // when
    const {
      status,
      data: {
        data: { studyStatistics },
        message,
      },
    } = await getMyStudyStatistics(client);
    // // when

    expect(status).toEqual(HttpStatusCode.Ok);
  });
  it("[200 OK] 사용자 리뷰 통계 조회 가능", async () => {
    // given
    const client = ApiClient.default();
    await signup(client, fakeSignupBody());

    // when
    const {
      status,
      data: {
        data: { reviewStatistics },
      },
    } = await getMyReviewStatistics(client);
    // // when

    expect(status).toEqual(HttpStatusCode.Ok);
    expect(reviewStatistics.activeness).toEqual(0);
    expect(reviewStatistics.communication).toEqual(0);
    expect(reviewStatistics.professionalism).toEqual(0);
    expect(reviewStatistics.recommend).toEqual(0);
    expect(reviewStatistics.together).toEqual(0);
  });

  it("[200 OK] 사용자 리뷰 통계 조회 가능", async () => {
    // given
    const client = ApiClient.default();
    await signup(client, fakeSignupBody());

    // when
    const {
      status,
      data: {
        data: { reviewStatistics },
      },
    } = await getMyReviewStatistics(client);
    // // when

    expect(status).toEqual(HttpStatusCode.Ok);
    expect(reviewStatistics.activeness).toEqual(0);
    expect(reviewStatistics.communication).toEqual(0);
    expect(reviewStatistics.professionalism).toEqual(0);
    expect(reviewStatistics.recommend).toEqual(0);
    expect(reviewStatistics.together).toEqual(0);
  });

  it("[201 CREATED] 로그인 시, 스터디 생성 가능", async () => {
    // given
    const client = ApiClient.default();
    const me = fakeSignupBody();
    await signup(client, me);

    // when
    const body = fakeCreateStudyRequest();
    const {
      status,
      data: {
        data: { study },
      },
    } = await createStudy(client, body);

    // then
    expect(status).toBe(HttpStatusCode.Created);
    expect(study.id).toBeDefined();
    expect(study.title).toEqual(body.title);
    expect(study.category.id).toEqual(body.categoryId);
    expect(study.platform).toEqual(body.platform);
    expect(study.platformUrl).toEqual(body.platformUrl);
    expect(study.owner.email).toEqual(me.email);
    expect(study.way).toEqual(body.way);
    expect(study.hasRecruitment).toEqual(false);
    expect(study.createdDateTime).toEqual(study.updatedDateTime);
    expect(study.createdDateTime).toEqual(study.updatedDateTime);
    expect(study.status).toEqual("RECRUITING");
  });

  test("[200 OK] review 작성 시, review 통계 확인 시 반영", async () => {
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

    await login(client, applicantUser);

    await getPeerReviews(client);
    const {
      status,
      data: {
        data: { reviewStatistics },
      },
    } = await getMyReviewStatistics(client);

    console.log(reviewStatistics)
    // then
    expect(status).toBe(HttpStatusCode.Ok);
    expect(reviewStatistics.activeness / 20).toBe(request.activenessScore);
    expect(reviewStatistics.communication / 20).toBe(
      request.communicationScore,
    );
    expect(reviewStatistics.professionalism / 20).toBe(
      request.professionalismScore,
    );
    expect(reviewStatistics.recommend / 20).toBe(request.recommendScore);
    expect(reviewStatistics.together / 20).toBe(request.togetherScore);
  });
});
