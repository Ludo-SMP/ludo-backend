// // TZ exists, but it's not handled.

// import { assert } from "console";

// export type NaiveDateTimeArgs = {
//   years: number;
//   months: number;
//   days: number;
//   hours: number;
//   minutes: number;
//   seconds: number;
//   milliseconds: number;
// };

// // automatically convert to UTC when intercepted by axios
// export class NaiveDateTime {
//   constructor(private readonly localDateTime: Date) {
//     this.localDateTime = localDateTime;
//   }

//   static create({
//     years,
//     months,
//     days,
//     hours,
//     minutes,
//     seconds,
//     milliseconds,
//   }: NaiveDateTimeArgs): NaiveDateTime {
//     const date = new Date(
//       years,
//       months - 1,
//       days,
//       hours,
//       minutes,
//       seconds,
//       milliseconds
//     );
//     return new NaiveDateTime(date).toUtc();
//   }

//   toUtc() {
//     const localOffset = new Date().getTimezoneOffset();
//     const localDateTime = new Date(
//       this.localDateTime.getTime() - localOffset * 60 * 1_000
//     );
//     return new NaiveDateTime(localDateTime);
//   }
//   toLocalDateTime() {
//     const localOffset = new Date().getTimezoneOffset();
//     const localDateTime = new Date(
//       this.localDateTime.getTime() + localOffset * 60 * 1_000
//     );
//     return new NaiveDateTime(localDateTime);
//   }

//   toLocalString() {
//     return this.toLocalDateTime().toString();
//   }

//   static fromUtcIsoString(utcString: string): NaiveDateTime {
//     return new NaiveDateTime(new Date(utcString));
//   }

//   static now(): NaiveDateTime {
//     return new NaiveDateTime(new Date()).toUtc();
//   }

//   toUtcString(): string {
//     // return this.localDateTime.toISOString().replace("Z", "+00:00");
//     return this.localDateTime.toISOString().slice(0, 22);
//   }

//   toString(): string {
//     return this.localDateTime.toISOString();
//   }
//   addYears(years: number): NaiveDateTime {
//     return this.addDays(_countDaysBetweenYears(this.year, this.year + years));
//   }

//   minusYears(years: number): NaiveDateTime {
//     return this.minusDays(_countDaysBetweenYears(this.year - years, this.year));
//   }

//   addMonths(months: number): NaiveDateTime {
//     if (months > 12) {
//       return this.addYears(months / 12).addMonths(months % 12);
//     }
//     const endMonth = this.month + months;
//     const endYear = endMonth < this.month ? this.year + 1 : this.year;

//     return this.addDays(
//       _countDaysOfMonthInRange(this.year, this.month, endYear, endMonth)
//     );
//   }

//   minusMonths(months: number): NaiveDateTime {
//     if (months > 12) {
//       return this.minusYears(months / 12).minusMonths(months % 12);
//     }
//     const startMonth = this.month - months;
//     const startYear = startMonth > this.month ? this.year - 1 : this.year;

//     return this.addDays(
//       _countDaysOfMonthInRange(startYear, startMonth, this.year, this.month)
//     );
//   }

//   addDays(days: number): NaiveDateTime {
//     return this.addHours(days * 24);
//   }

//   addHours(hours: number): NaiveDateTime {
//     return this.addMinutes(hours * 60);
//   }

//   addMinutes(minutes: number): NaiveDateTime {
//     return this.addSeconds(minutes * 60);
//   }

//   addSeconds(seconds: number): NaiveDateTime {
//     return this.addMillis(seconds * 1_000);
//   }

//   addMillis(millis: number): NaiveDateTime {
//     return new NaiveDateTime(new Date(this.localDateTime.getTime() + millis));
//   }

//   minusDays(days: number): NaiveDateTime {
//     return this.addDays(-days);
//   }

//   minusHours(hours: number): NaiveDateTime {
//     return this.addHours(-hours);
//   }

//   minusMinutes(minutes: number): NaiveDateTime {
//     return this.addMinutes(-minutes);
//   }

//   minusSeconds(seconds: number): NaiveDateTime {
//     return this.addSeconds(-seconds);
//   }

//   minusMillis(millis: number): NaiveDateTime {
//     return this.addMillis(-millis);
//   }

//   get unixTime() {
//     return this.localDateTime.getTime();
//   }

//   get year() {
//     return this.localDateTime.getFullYear();
//   }

//   get month() {
//     return this.localDateTime.getMonth() + 1;
//   }

//   get day() {
//     return this.localDateTime.getDate();
//   }

//   get hour() {
//     return this.localDateTime.getHours();
//   }

//   get minute() {
//     return this.localDateTime.getMinutes();
//   }

//   get second() {
//     return this.localDateTime.getSeconds();
//   }

//   get millisecond() {
//     return this.localDateTime.getMilliseconds();
//   }

//   get dayOfWeek() {
//     return this.localDateTime.getDay();
//   }
// }

// function _countDaysBetweenYears(startYear: number, endYear: number): number {
//   const countLeapYears = _countLeapYearsBetween(startYear, endYear);
//   return (endYear - startYear) * 365 + countLeapYears;
// }

// function _countLeapYearsBetween(startYear: number, endYear: number): number {
//   return _countLeapYearsUntil(endYear - 1) - _countLeapYearsUntil(startYear);
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

//   if (endYear === startMonth) {
//     return Array(endMonth - startMonth).reduce(
//       (totalDays, month) => totalDays + _countDaysOfMonth(endYear, month),
//       0
//     );
//   }

//   const daysOfCurrentYear = Array(12 - startMonth).reduce(
//     (totalDays, month) => totalDays + _countDaysOfMonth(startYear, month),
//     0
//   );
//   const daysOfNextYear = Array(endMonth).reduce(
//     (totalDays, month) => totalDays + _countDaysOfMonth(endYear, month),
//     0
//   );
//   return daysOfCurrentYear + daysOfNextYear;
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
