import { AxiosError, HttpStatusCode } from "axios";
import dns from "node:dns";
import { describe } from "node:test";
import { ApiClient } from "../config/api-client";
import { fakeSignupBody } from "../fixtures/auth-fixture";
import { randPositionId } from "../fixtures/position-fixture";
import { fakeWriteRecruitmentRequest } from "../fixtures/recruitment-fixture";
import { fakeCreateStudyRequest } from "../fixtures/study-fixture";
import { login, signup } from "../helpers/auth-api-helper";
import { applyRecruitment, writeRecruitment } from "../helpers/recruitment-api-helper";
import {
  acceptApplicant,
  createStudy,
  findApplicantsByStudyId,
  findStudyDetailById,
  refuseApplicant,
  updateStudy,
} from "../helpers/study-api-helper";
import { BaseResponse } from "../types/base-types";
import { Study } from "../types/study-types";
dns.setDefaultResultOrder("ipv4first");

describe("study Api flows", () => {
  it("[400 BAD REQUEST] 로그인 안 하면 스터디 생성 불가", async () => {
    // given
    const client = ApiClient.default();

    // when
    // then
    try {
      const body = fakeCreateStudyRequest();
      await createStudy(client, body);
    } catch (err) {
      const e = err as AxiosError<BaseResponse<Study>>;
      expect(e.response?.status).toBe(HttpStatusCode.BadRequest);
      expect(e.response!.data.message).toBe("토큰이 없습니다.");
    }
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

  // no token returns 400
  it("[400 BAD REQUEST] 로그인 안 된 사용자는 스터디 생성 불가", async () => {
    // it("[401 UNAUTHORIZED] 로그인 안 된 사용자는 스터디 생성 불가", async () => {
    // given
    const client = ApiClient.default();

    // when
    // then
    try {
      await createStudy(client, fakeCreateStudyRequest());
    } catch (err) {
      const e = err as AxiosError<BaseResponse<void>>;
      // expect(e.response!.status).toBe(HttpStatusCode.Unauthorized);
      expect(e.response!.status).toBe(HttpStatusCode.BadRequest);
      // expect(e.response!.data.message).toBe("로그인이 필요한 서비스입니다.");
      expect(e.response!.data.message).toBe("토큰이 없습니다.");
    }
  });

  it("should a new study if user logged in", async () => {
    // given
    const client = ApiClient.default();
    const me = fakeSignupBody();
    await signup(client, me);

    // when
    const body = fakeCreateStudyRequest();
    const {
      data: {
        data: {
          study: { id: studyId },
        },
      },
    } = await createStudy(client, body);
    const {
      status,
      data: {
        data: { study: updatedStudy },
      },
    } = await updateStudy(client, studyId, {
      ...body,
      title: "updated title",
    });

    // then
    expect(status).toBe(HttpStatusCode.Ok);
    expect(updatedStudy.id).toEqual(studyId);
    expect(updatedStudy.title).toEqual("updated title");
  });

  describe("스터디 지원/수락", () => {
    it("[200 OK] 스터디장은 지원자 수락 가능", async () => {
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
      const { data: writeRecruitmentData } = await writeRecruitment(
        client,
        studyId,
        fakeWriteRecruitmentRequest(),
      );
      const recruitmentId = writeRecruitmentData.data.recruitment.id;
      const {
        data: {
          data: { user: applicant },
        },
      } = await signup(client, fakeSignupBody());
      await applyRecruitment(client, studyId, recruitmentId, {
        positionId: randPositionId(),
      });

      // when
      await login(client, owner);
      const {
        status,
        data: {
          data: { participant },
        },
      } = await acceptApplicant(client, studyId, applicant.id);

      // then
      expect(status).toEqual(HttpStatusCode.Ok);
      expect(participant.email).toEqual(applicant.email);
      expect(participant.nickname).toEqual(applicant.nickname);
    });
  });

  test("[200 OK] 스터디장은 지원자 수락 및 거절 가능", async () => {
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

    // applicantUser1
    const applicantUser1 = fakeSignupBody();
    const {
      data: {
        data: {
          user: { id: applicantUser1Id },
        },
      },
    } = await signup(client, applicantUser1);
    await applyRecruitment(client, studyId, recruitment.id, {
      positionId: randPositionId(),
    });
    // // applicantUser2
    const applicantUser2 = fakeSignupBody();
    const {
      data: {
        data: {
          user: { id: applicantUser2Id },
        },
      },
    } = await signup(client, applicantUser2);
    await applyRecruitment(client, studyId, recruitment.id, {
      positionId: randPositionId(),
    });

    // login to owner
    await login(client, owner);

    // when accepted
    const {
      status: acceptedStatus,
      data: {
        data: { participant: acceptedParticipant },
      },
    } = await acceptApplicant(client, studyId, applicantUser1Id);

    // then
    expect(acceptedStatus).toBe(HttpStatusCode.Ok);
    expect(acceptedParticipant.id).toBeDefined();
    expect(acceptedParticipant.email).toEqual(applicantUser1.email);
    expect(acceptedParticipant.nickname).toEqual(applicantUser1.nickname);

    const {
      status: acceptedStudyStatus,
      data: {
        data: { study: acceptedStudy },
      },
    } = await findStudyDetailById(client, studyId);

    expect(acceptedStudyStatus).toBe(HttpStatusCode.Ok);
    expect(acceptedStudy.participantCount).toEqual(2);
    const filteredParticipant = acceptedStudy.participants.filter(
      (p) => p.id === applicantUser1Id,
    )[0];
    expect(filteredParticipant.id).toEqual(applicantUser1Id);

    // when refused
    const { status: refusedStudyStatus } = await refuseApplicant(
      client,
      studyId,
      applicantUser2Id,
    );

    // then
    expect(refusedStudyStatus).toBe(HttpStatusCode.Ok);

    const {
      status: findStudyStatus,
      data: {
        data: { study },
      },
    } = await findStudyDetailById(client, studyId);

    expect(findStudyStatus).toBe(HttpStatusCode.Ok);
    expect(study.participantCount).toEqual(2);
    const participated = study.participants.find(
      (p) => p.id === applicantUser2Id,
    );
    expect(participated).toBeUndefined();
  });

  test("[200 OK] 스터디장이 지원자들의 정보를 확인할 때, Review 통계 조회 가능", async () => {
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

    const applicantUser1 = fakeSignupBody();
    const {
      data: {
        data: {
          user: { id: applicantUser1Id },
        },
      },
    } = await signup(client, applicantUser1);
    await applyRecruitment(client, studyId, recruitment.id, {
      positionId: randPositionId(),
    });

    const applicantUser2 = fakeSignupBody();
    const {
      data: {
        data: {
          user: { id: applicantUser2Id },
        },
      },
    } = await signup(client, applicantUser2);
    await applyRecruitment(client, studyId, recruitment.id, {
      positionId: randPositionId(),
    });

    const applicantUser3 = fakeSignupBody();
    const {
      data: {
        data: {
          user: { id: applicantUser3Id },
        },
      },
    } = await signup(client, applicantUser3);
    await applyRecruitment(client, studyId, recruitment.id, {
      positionId: randPositionId(),
    });

    // login to owner
    await login(client, owner);
    const { status, data: { data: { applicants } } } = await findApplicantsByStudyId(client, studyId);

    //
    expect(status).toBe(HttpStatusCode.Ok);
    expect(applicants.length).toBe(3);
  });
});
