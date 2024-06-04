export type Transformer = (...args: unknown[]) => AnyOrUndefined;
// for explicitly deferenticate Optional Types
export type AnyOrUndefined = any | undefined;

export function transformObj(obj: any, ...tfs: Transformer[]): any {
  if (obj === null || obj === undefined) {
    return obj;
  }

  for (const tf of tfs) {
    const result = tf(obj);
    if (result) {
      return result;
    }
  }

  if (Array.isArray(obj)) {
    return obj.map((subObj) => transformObj(subObj, ...tfs));
  }

  if (typeof obj === "object") {
    for (const [k, v] of Object.entries(obj)) {
      obj[k] = transformObj(v, ...tfs);
    }
    return obj;
  }

  return obj;
}
