// import { applyOffset } from "@formkit/tempo";
// // TZ exists, but it's not handled.

// import assert from "assert";

// export type LocalDateTimeArgs = {
//   years: number;
//   months: number;
//   days: number;
//   hours: number;
//   minutes: number;
//   seconds: number;
//   milliseconds: number;
// };

// // automatically convert from UTC to Locale when intercepted by axios
// export class LocalDateTime {
//   constructor(private readonly inner: Date) {
//     this.inner = inner;
//   }

//   static create({
//     years,
//     months,
//     days,
//     hours,
//     minutes,
//     seconds,
//     milliseconds,
//   }: LocalDateTimeArgs): LocalDateTime {
//     const date = new Date(
//       years,
//       months - 1,
//       days,
//       hours,
//       minutes,
//       seconds,
//       milliseconds
//     );
//     return new LocalDateTime(date);
//   }

//   static fromUtcString(utcString: string): LocalDateTime {
//     const d = applyOffset(new Date(utcString), "+09:00");
//     return new LocalDateTime(d);
//   }

//   static now(): LocalDateTime {
//     return new LocalDateTime(new Date());
//   }

//   toUtcDateTime() {
//     const d = applyOffset(new Date(this.inner), "-09:00");
//     return new LocalDateTime(d);
//   }

//   toLocalDateTime() {
//     const localOffset = new Date().getTimezoneOffset();
//     const localDateTime = new Date(
//       this.inner.getTime() + localOffset * 60 * 1_000
//     );
//     return new LocalDateTime(localDateTime);
//   }

//   toUtcString(): string {
//     return this.toUtcDateTime().toString();
//   }

//   toString(): string {
//     return this.inner.toISOString().slice(0, 23);
//   }

//   addYears(years: number): LocalDateTime {
//     const prevYear = this.year;
//     const nextYear = this.year + years;
//     let nextMonth = this.month;
//     let nextDate = this.day;
//     // 1. + 윤년 -> 평년 24/02/29 -> 25/03/01
//     // 2. - 윤년 -> 평년 24/02/29 -> 23/02/28

//     if (
//       this.month === 2 &&
//       this.day === 29 &&
//       _isLeapYear(prevYear) &&
//       !_isLeapYear(nextYear)
//     ) {
//       if (prevYear < nextYear) {
//         nextMonth = 3;
//         nextDate = 1;
//       } else if (prevYear > nextYear) {
//         nextMonth = 2;
//         nextDate = 28;
//       }
//     }
//     console.log(
//       `prevYear: ${prevYear}, nextYear: ${nextYear}, nextDate: ${nextDate}, nextMonth: ${nextMonth}`
//     );

//     const d = LocalDateTime.create({
//       years: nextYear,
//       months: nextMonth,
//       days: nextDate,
//       hours: this.hour,
//       minutes: this.minute,
//       seconds: this.second,
//       milliseconds: this.millisecond,
//     });
//     console.log(new Date(2025, 2, 1));
//     return LocalDateTime.create({
//       years: nextYear,
//       months: nextMonth,
//       days: nextDate,
//       hours: this.hour,
//       minutes: this.minute,
//       seconds: this.second,
//       milliseconds: this.millisecond,
//     });
//   }

//   minusYears(years: number): LocalDateTime {
//     const d = this.toUtcDateTime();
//     return this.minusDays(
//       _countDaysBetweenYears(d.year - years + 1, d.year + 1)
//     );
//   }

//   addMonths(months: number): LocalDateTime {
//     const d = this.toUtcDateTime();
//     if (months > 12) {
//       return this.addYears(months / 12).addMonths(months % 12);
//     }
//     const endMonth = ((d.month + months - 1) % 12) + 1;
//     console.log(`endMonth: ${endMonth}`);
//     const endYear = endMonth < d.month ? d.year + 1 : d.year;

//     return this.addDays(
//       _countDaysOfMonthInRange(d.year, d.month, endYear, endMonth)
//     );
//   }

//   minusMonths(months: number): LocalDateTime {
//     const d = this.toUtcDateTime();
//     if (months > 12) {
//       return this.minusYears(months / 12).minusMonths(months % 12);
//     }
//     const startMonth = d.month - months;
//     const startYear = startMonth > d.month ? d.year - 1 : d.year;

//     return this.minusDays(
//       _countDaysOfMonthInRange(startYear, startMonth, d.year, d.month)
//     );
//   }

//   addDays(days: number): LocalDateTime {
//     return this.addHours(days * 24);
//   }

//   addHours(hours: number): LocalDateTime {
//     return this.addMinutes(hours * 60);
//   }

//   addMinutes(minutes: number): LocalDateTime {
//     return this.addSeconds(minutes * 60);
//   }

//   addSeconds(seconds: number): LocalDateTime {
//     return this.addMillis(seconds * 1_000);
//   }

//   addMillis(millis: number): LocalDateTime {
//     return new LocalDateTime(new Date(this.inner.getTime() + millis));
//   }

//   minusDays(days: number): LocalDateTime {
//     return this.addDays(-days);
//   }

//   minusHours(hours: number): LocalDateTime {
//     return this.addHours(-hours);
//   }

//   minusMinutes(minutes: number): LocalDateTime {
//     return this.addMinutes(-minutes);
//   }

//   minusSeconds(seconds: number): LocalDateTime {
//     return this.addSeconds(-seconds);
//   }

//   minusMillis(millis: number): LocalDateTime {
//     return this.addMillis(-millis);
//   }

//   get unixTime() {
//     return this.inner.getTime();
//   }

//   get year() {
//     return this.inner.getFullYear();
//   }

//   get monthIndex() {
//     return this.inner.getMonth();
//   }

//   get month() {
//     return this.inner.getMonth() + 1;
//   }

//   get day() {
//     return this.inner.getDate();
//   }

//   get hour() {
//     return this.inner.getHours();
//   }

//   get minute() {
//     return this.inner.getMinutes();
//   }

//   get second() {
//     return this.inner.getSeconds();
//   }

//   get millisecond() {
//     return this.inner.getMilliseconds();
//   }

//   get dayOfWeek() {
//     return this.inner.getDay();
//   }
// }

// function _countDaysBetweenYears(startYear: number, endYear: number): number {
//   const countLeapYears = _countLeapYearsBetween(startYear, endYear);
//   return (endYear - startYear) * 365 + countLeapYears;
// }

// function _countLeapYearsBetween(startYear: number, endYear: number): number {
//   return _countLeapYearsUntil(endYear) - _countLeapYearsUntil(startYear);
// }

// function _countLeapYearsUntil(year: number): number {
//   return Math.floor(year / 4) - Math.floor(year / 100) + Math.floor(year / 400);
// }

// function _isLeapYear(year: number) {
//   assert(year > 0);
//   return year % 4 === 0 && (year % 100 !== 0 || year % 400 === 0);
// }

// const DAYS_PRESUM = new Array(12);

// function _initDays() {
//   for (let i = 0; i < 12; i++) {
//     DAYS_PRESUM[i] = _countDaysOfMonth(2_001, i + 1);
//     DAYS_PRESUM[i] += DAYS_PRESUM.slice(0, i).reduce((r, n) => r + n, 0);
//   }
// }
// _initDays();

// function _countDaysUntil(year: number, month: number): number {
//   assert(month > 0 && month < 13);
//   let daysUntil = DAYS_PRESUM[month];
//   if (_isLeapYear(year) && month >= 2) {
//     daysUntil++;
//   }
//   return daysUntil;
// }

// function _countDaysOfMonthInRange(
//   startYear: number,
//   startMonth: number,
//   endYear: number,
//   endMonth: number
// ): number {
//   assert(startMonth > 0 && startMonth < 13);
//   assert(endMonth > 0 && endMonth < 13);
//   assert(startYear < endYear || startMonth <= endMonth);

//   console.log(
//     `startYear: ${startYear}, startMonth: ${startMonth}, endYear: ${endYear}, endMonth: ${endMonth}`
//   );

//   let sumOfDays = 0;
//   if (endYear === startYear) {
//     for (let m = startMonth; m < endMonth; m++) {
//       console.log(_countDaysOfMonth(startYear, m));

//       sumOfDays += _countDaysOfMonth(startYear, m);
//     }
//     return sumOfDays;
//   }

//   for (let m = startMonth; m <= 12; m++) {
//     sumOfDays += _countDaysOfMonth(startYear, m);
//   }

//   for (let m = 1; m < endMonth; m++) {
//     sumOfDays += _countDaysOfMonth(endYear, m);
//   }

//   console.log(`sum: ${sumOfDays}`);
//   return sumOfDays;
// }

// function _countDaysOfMonth(year: number, month: number): number {
//   switch (month) {
//     case 1:
//     case 3:
//     case 5:
//     case 7:
//     case 8:
//     case 10:
//     case 12:
//       return 31;
//     case 4:
//     case 6:
//     case 9:
//     case 11:
//       return 30;
//     case 2:
//       return _isLeapYear(year) ? 29 : 28;
//     default:
//       throw new Error("Invalid month");
//   }
// }

// function _leftPadZero(num: number, len: number = 2): string {
//   let str = num.toString();
//   while (str.length < len) {
//     str = "0" + str;
//   }
//   return str;
// }
