package com.ludo.study.studymatchingplatform.common.auditing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h5>
 * `EntityListeners` Annotation을 원하는 Entity에 부착
 * 이후 원하는 필드에 `PreUpdateUtcNow`를 붙이면, update 시 자동으로
 * `UtcDateTimePicker.now`를 호출하여 현재 시간을 얻음.
 * 또한 create도 update로 간주하여 생성 시에도 `NULL` 대신 값 대입
 * <p>
 * 테스트 시에는 Profile을 "test"로 설정하면 `UtcDateTimePicker`는
 * `FixedUtcDateTimePicker`로 교체되기 때문에, 2000-01-01로 고정된 값이 나오게 됨
 * 테스트 시에 `FixedUtcDateTimePicker`의 구현체 내에 `plusDays` 등 원하는 메소드를 만들어서
 * `now()`가 반환하는 값을 원하는 값으로 변경하여 테스트 할 수 있음
 * <p>
 * 이 때는 Autowired로 주입 받는 것이 인터페이스가 아닌 `FixedUtcDateTimePicker`여야 인터페이스 노출
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
 *     &#064;PreUpdateUtcNow
 *     LocalDateTime updated;
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
 *     FixedUtcDateTimePicker fixedUtcDateTimePicker;
 *
 *     &#064;Autowired
 *     EntityManager em;
 *
 *     &#064;Test
 *     void test()  {
 *          Person person = new Person();
 *          person.name = "x";
 *          em.persist(person);
 *
 *          em.flush();
 *          em.clear();
 *
 *          // plusSeconds manually
 *          fixedUtcDateTimePicker.plusSeconds(1000);
 *
 *          var found = em.find(Person.class, person.id);
 *          // updated. `@PreUpdateUtcNow` will be fired on next commit
 *          found.name = "y";
 *     }
 * }
 * </pre>
 *
 * <h5>Result1</h5>
 * name = "x"
 * updated = 2000-01-01T00:00
 *
 * <h5>Result2</h5>
 * name = "y"
 * updated = 2000-01-01T16:40
 *
 * @see PrePersistUtcNow
 * @see UtcNowAuditor
 * @see com.ludo.study.studymatchingplatform.common.utils.UtcDateTimePicker
 * @see com.ludo.study.studymatchingplatform.study.service.study.FixedUtcDateTimePicker
 * @see com.ludo.study.studymatchingplatform.common.utils.CurrentUtcDateTimePicker
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreUpdateUtcNow {
}