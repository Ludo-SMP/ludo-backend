import { HttpStatusCode } from "axios";
import { Knex } from "knex";
import { ApiClient } from "../config/api-client";
import { fakeSignupBody } from "../fixtures/auth-fixture";
import { randPositionId } from "../fixtures/position-fixture";
import { fakeWriteRecruitmentRequest } from "../fixtures/recruitment-fixture";
import { fakeCreateStudyRequest } from "../fixtures/study-fixture";
import { login, signup } from "../helpers/auth-api-helper";
import {
  applyRecruitment,
  findRecruitmentById,
  writeRecruitment,
} from "../helpers/recruitment-api-helper";
import { createStudy, refuseApplicant } from "../helpers/study-api-helper";

let tx: Knex.Transaction<any, any[]>;

describe("recruitment Api flows", () => {
  test("[200 OK] 스터디장은 모집 공고 작성 가능", async () => {
    // given
    const client = ApiClient.default();
    const me = fakeSignupBody();
    await signup(client, me);
    const {
      data: {
        data: { study: createdStudy },
      },
    } = await createStudy(client, fakeCreateStudyRequest());

    // when
    const body = fakeWriteRecruitmentRequest();
    const {
      status,
      data: {
        data: { recruitment },
      },
    } = await writeRecruitment(client, createdStudy.id, body);

    // then
    expect(status).toBe(HttpStatusCode.Created);
    expect(recruitment.id).toBeDefined();
    // applicantCount가 write 시에는 Limit으로,
    // response에는 실제로 적용된 applicantCount가 들어있음
    // -> 혼동 되는 부분이지만 일단 명세대로 놔둠
    expect(recruitment.applicantCount).toEqual(0);
    expect(recruitment.callUrl).toEqual(body.callUrl);
    expect(recruitment.contact).toEqual(body.contact);
    expect(recruitment.content).toEqual(body.content);
    expect(recruitment.positions.length).toEqual(body.positionIds.length);
    expect(recruitment.stacks.length).toEqual(body.stackIds.length);
    expect(recruitment.title).toEqual(body.title);
    // expect(recruitment.endDateTime).toEqual(body.recruitmentEndDateTime);
  });
  test("[200 OK] 모집 중인 모집 공고에 지원 가능", async () => {
    // given
    const client = ApiClient.default();
    const {
      data: {
        data: { user: owner },
      },
    } = await signup(client, fakeSignupBody());
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

    // when
    const {
      data: {
        data: { user: applicantUser },
      },
    } = await signup(client, fakeSignupBody());
    const {
      status,
      data: {
        data: { applicantId },
      },
    } = await applyRecruitment(client, studyId, recruitment.id, {
      positionId: randPositionId(),
    });

    // then
    expect(status).toBe(HttpStatusCode.Ok);
    expect(applicantId).toBeDefined();
  });
  test("[200 OK] id로 모집 공고 조회 가능", async () => {
    // given
    const client = ApiClient.default();
    await signup(client, fakeSignupBody());
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
      fakeWriteRecruitmentRequest()
    );
    const recruitmentId = writeRecruitmentData.data.recruitment.id;
    await signup(client, fakeSignupBody());
    await applyRecruitment(client, studyId, recruitmentId, {
      positionId: randPositionId(),
    });

    // when
    const {
      status,
      data: {
        data: { recruitment },
      },
    } = await findRecruitmentById(client, recruitmentId);

    // then
    expect(status).toBe(HttpStatusCode.Ok);
    expect(recruitment.applicantCount).toEqual(1);
    expect(recruitment.id).toEqual(recruitmentId);
  });
  test("[200 OK] 스터디장은 모집 공고 지원자 거절 가능", async () => {
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
      fakeWriteRecruitmentRequest()
    );
    const recruitmentId = writeRecruitmentData.data.recruitment.id;
    const {
      data: {
        data: {
          user: { id: applicantId },
        },
      },
    } = await signup(client, fakeSignupBody());
    await applyRecruitment(client, studyId, recruitmentId, {
      positionId: randPositionId(),
    });

    // when
    // then
    await login(client, owner);
    const { status: refuseApplicantStatus } = await refuseApplicant(
      client,
      studyId,
      applicantId
    );
    expect(refuseApplicantStatus).toBe(HttpStatusCode.Ok);

    const {
      status,
      data: {
        data: { recruitment },
      },
    } = await findRecruitmentById(client, recruitmentId);

    // then
    expect(status).toBe(HttpStatusCode.Ok);
    expect(recruitment.applicantCount).toEqual(1);
    expect(recruitment.id).toEqual(recruitmentId);
  });
  
  test("[403 FORBIDDEN] 스터디장이 아니면 모집 공고 작성 불가", async () => {});
  test("[400 BAD_REQUEST] 스터디장은 스스로의 모집 공고에 지원 불가", async () => {});
  test("[409 CONFLICT] 이미 모집 공고가 작성된 스터디의 경우 추가 작성 불가", async () => {});
});
