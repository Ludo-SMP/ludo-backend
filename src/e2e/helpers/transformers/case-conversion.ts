export function camelPropsToSnake(obj: any): any {
  const snakeObj: any = {};
  if (typeof obj === "object") {
    for (const [k, v] of Object.entries(obj)) {
      delete snakeObj[k];
      snakeObj[_camelToSnake(k)] = v;
    }
  }
  return snakeObj;
}

function _camelToSnake(camel: string): string {
  const snake = camel.replace(/[A-Z]/g, (letter) => `_${letter.toLowerCase()}`);
  if (snake.startsWith("_")) {
    return snake.slice(1);
  }
  return snake;
}
