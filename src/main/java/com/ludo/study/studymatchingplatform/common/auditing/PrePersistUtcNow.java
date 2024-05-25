package com.ludo.study.studymatchingplatform.common.auditing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h5>
 * `EntityListeners` Annotation을 원하는 Entity에 부착
 * 이후 원하는 필드에 `PrePersistUtcNow`를 붙이면, persist 시 자동으로
 * `UtcDateTimePicker.now`를 호출하여 현재 시간을 얻음.
 * <p>
 * 테스트 시에는 Profile을 "test"로 설정하면 `UtcDateTimePicker`는
 * `FixedUtcDateTimePicker`로 교체되기 때문에, 2000-01-01로 고정된 값이 나오게 됨
 * <p>
 * 테스트 시 참고
 *
 * </h5>
 * <h4> Example </h4>
 * <pre>
 * &#064;Entity
 * &#064;Getter
 * &#064;EntityListeners(UtcNowAuditor.class)
 * public class Person {
 *
 *     &#064;Id
 *     &#064;GeneratedValue
 *     Long id;
 *
 *     String name;
 *
 *     &#064;PrePersistUtcNow
 *     LocalDateTime created;
 *
 * }
 * </pre>
 * <p>
 *
 * <h5>Test Example</h5>
 * <pre>
 * &#064;SpringBootTest
 * &#064;Transactional
 * &#064;Commit
 * &#064;ActiveProfiles("test")
 * class TestExample {
 *
 *     &#064;Autowired
 *     UtcDateTimePicker utcDateTimePicker;
 *
 *     &#064;Autowired
 *     EntityManager em;
 *
 *     &#064;Test
 *     void test()  {
 *          Person person = new Person();
 *          person.name = "x";
 *          em.persist(person);
 *     }
 * }
 * </pre>
 *
 * <h5>Result</h5>
 * name = "x"
 * created = 2000-01-01T00:00
 *
 * @see PreUpdateUtcNow
 * @see UtcNowAuditor
 * @see com.ludo.study.studymatchingplatform.common.utils.UtcDateTimePicker
 * @see com.ludo.study.studymatchingplatform.study.service.study.FixedUtcDateTimePicker
 * @see com.ludo.study.studymatchingplatform.common.utils.CurrentUtcDateTimePicker
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrePersistUtcNow {
}