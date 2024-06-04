const preset = require("ts-jest/presets");

/** @type {import('ts-jest').JestConfigWithTsJest} */
module.exports = {
  ...preset.defaults,
  setupFiles: ["dotenv/config"],
  transform: {
    "^.+\\.ts?$": [
      "ts-jest",
      {
        isolatedModules: true,
      },
    ],
  },
  cache: true,
  detectOpenHandles: true,
  verbose: true,
  setupFilesAfterEnv: ["./jest.setup.ts"],
};
