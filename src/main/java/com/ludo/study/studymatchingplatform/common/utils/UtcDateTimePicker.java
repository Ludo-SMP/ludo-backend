package com.ludo.study.studymatchingplatform.common.utils;

import java.time.LocalDateTime;

/**
 * <h5>테스트 시, Profile을 "test"로 설정하면 항상 2000-01-01으로 고정된 DateTime 획득 </h5>
 * <h5>Profile가 "test"가 아닐 경우, `now`는 현재 시각 반환. 단, UTC+00에 고정되며, microseconds 이하는 truncate</h5>
 *
 *
 *
 * <h4> ========Example======= </h3>
 * <pre>
 * &#064;SpringBootTest
 * &#064;ActiveProfiles("test")
 * public class Test {
 *     &#064;Autowired
 *     UtcDateTimePicker utcDateTimePicker;
 *
 *     &#064;Test
 *     void test() {
 *         // 2000-01-01 고정 UTC Time
 *         LocalDateTime dt = utcDateTimePicker.now();
 *     }
 *
 * }
 * </pre>
 *
 * @see com.ludo.study.studymatchingplatform.study.service.study.FixedUtcDateTimePicker
 * @see CurrentUtcDateTimePicker
 */
public interface UtcDateTimePicker {
    LocalDateTime now();
}
