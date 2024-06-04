import { applyOffset } from "@formkit/tempo";
import { AnyOrUndefined } from "./transformers";

export function transformFromIsoStringToNaiveDateTime(
  obj: any
): AnyOrUndefined {
  if (isIsoDateString(obj)) {
    return applyOffset(new Date(obj), "+09:00");
  }
  return undefined;
}

export function transformFromNaiveTimeToIsoString(obj: any): AnyOrUndefined {
  if (isIsoDateString(obj)) {
    return applyOffset(new Date(obj), "-09:00");
  }
  return undefined;
}

// export function transformFromNaiveTimeToIsoString(obj: any): AnyOrUndefined {

//   if (obj instanceof LocalDateTime) {
//     return obj.toUtcString();
//   }
//   return undefined;
// }

// export function transformFromIsoStringToNaiveDateTime(
//   obj: any
// ): AnyOrUndefined {
//   if (isIsoDateString(obj)) {
//     return LocalDateTime.fromUtcString(obj);
//   }
//   return undefined;
// }

function isIsoDateString(value: any): boolean {
  if (typeof value !== "string") {
    return false;
  }

  // ISO 8601 Format DateTime String
  const isoDateStringRegex =
    /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(\.\d*)?(Z|([+-]\d{2}:\d{2}))?$/;
  return isoDateStringRegex.test(value);
}
