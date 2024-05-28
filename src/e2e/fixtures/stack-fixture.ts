import { faker } from "@faker-js/faker";
import { randInt } from "./rand-fixture";

export type PersistStack = {
  stackId: number;
  name: string;
  imageUrl: string;
  companyCount: number;
  stackCategoryId: number;
};

export type PersistStackCategory = {
  stackCategoryId: number;
  name: string;
};

export const STACK_CATEGORIES: PersistStackCategory[] = [
  {
    stackCategoryId: 1,
    name: "Frontend",
  },
  {
    stackCategoryId: 2,
    name: "Backend",
  },
  {
    stackCategoryId: 3,
    name: "Mobile",
  },
  {
    stackCategoryId: 4,
    name: "DevOps",
  },
  {
    stackCategoryId: 5,
    name: "Language",
  },
  {
    stackCategoryId: 6,
    name: "Other",
  },
];

export const STACKS: PersistStack[] = [
  {
    stackId: 1,
    name: "Axum",
    imageUrl: "https://avatars.githubusercontent.com/u/20248544?s=96",
    companyCount: randInt(1, 1000),
    stackCategoryId: 2,
  },
  {
    stackId: 2,
    name: "Pavex",
    imageUrl:
      "https://raw.githubusercontent.com/LukeMathWalker/pavex/main/logo-dark.webp#gh-dark-mode-only",
    companyCount: randInt(1, 1000),
    stackCategoryId: 2,
  },
  {
    stackId: 3,
    name: "Rocket",
    imageUrl: "https://avatars.githubusercontent.com/u/106361765?s=96",
    companyCount: randInt(1, 1000),
    stackCategoryId: 2,
  },
  {
    stackId: 4,
    name: "Armeria",
    imageUrl:
      "https://image.wanted.co.kr/optimize?src=https://static.codenary.co.kr/framework_logo/armeria.png&w=150&q=90",
    companyCount: randInt(1, 1000),
    stackCategoryId: 2,
  },
  {
    stackId: 5,
    name: "Flutter",
    imageUrl:
      "https://image.wanted.co.kr/optimize?src=https://static.codenary.co.kr/framework_logo/flutter.png&w=150&q=90",
    companyCount: randInt(1, 1000),
    stackCategoryId: 3,
  },
  {
    stackId: 6,
    name: "Lottie",
    imageUrl:
      "https://image.wanted.co.kr/optimize?src=https://static.codenary.co.kr/framework_logo/lottie.png&w=150&q=90",
    companyCount: randInt(1, 1000),
    stackCategoryId: 3,
  },
  {
    stackId: 7,
    name: "RIBs",
    imageUrl:
      "https://image.wanted.co.kr/optimize?src=https://static.codenary.co.kr/framework_logo/ribs.png&w=150&q=90",
    companyCount: randInt(1, 1000),
    stackCategoryId: 3,
  },
  {
    stackId: 8,
    name: "Fastlane",
    imageUrl:
      "https://image.wanted.co.kr/optimize?src=https://static.codenary.co.kr/framework_logo/fastlane.png&w=150&q=90",
    companyCount: randInt(1, 1000),
    stackCategoryId: 3,
  },
  {
    stackId: 9,
    name: "ArgoCD",
    imageUrl:
      "https://image.wanted.co.kr/optimize?src=https://static.codenary.co.kr/framework_logo/argocd.png&w=150&q=90",
    companyCount: randInt(1, 1000),
    stackCategoryId: 4,
  },
  {
    stackId: 10,
    name: "Envoy",
    imageUrl:
      "https://image.wanted.co.kr/optimize?src=https://static.codenary.co.kr/framework_logo/envoy.png&w=150&q=90",
    companyCount: randInt(1, 1000),
    stackCategoryId: 4,
  },
  {
    stackId: 11,
    name: "Gulp",
    imageUrl:
      "https://image.wanted.co.kr/optimize?src=https://static.codenary.co.kr/framework_logo/gulp.png&w=150&q=90",
    companyCount: randInt(1, 1000),
    stackCategoryId: 1,
  },
  {
    stackId: 12,
    name: "Jaeger",
    imageUrl:
      "https://image.wanted.co.kr/optimize?src=https://static.codenary.co.kr/framework_logo/jaeger.png&w=150&q=90",
    companyCount: randInt(1, 1000),
    stackCategoryId: 4,
  },
  {
    stackId: 13,
    name: "Nexus",
    imageUrl:
      "https://image.wanted.co.kr/optimize?src=https://static.codenary.co.kr/framework_logo/nexus.png&w=150&q=90",
    companyCount: randInt(1, 1000),
    stackCategoryId: 4,
  },
  {
    stackId: 14,
    name: "Helm",
    imageUrl:
      "https://image.wanted.co.kr/optimize?src=https://static.codenary.co.kr/framework_logo/helm.png&w=150&q=90",
    companyCount: randInt(1, 1000),
    stackCategoryId: 4,
  },
  {
    stackId: 15,
    name: "Phoenix",
    imageUrl:
      "https://image.wanted.co.kr/optimize?src=https://static.codenary.co.kr/framework_logo/phoenix.png&w=150&q=90",
    companyCount: randInt(1, 1000),
    stackCategoryId: 1,
  },
  {
    stackId: 16,
    name: "Angular",
    imageUrl:
      "https://image.wanted.co.kr/optimize?src=https://static.codenary.co.kr/framework_logo/angular.png&w=150&q=90",
    companyCount: randInt(1, 1000),
    stackCategoryId: 1,
  },
  {
    stackId: 17,
    name: "Ember",
    imageUrl:
      "https://image.wanted.co.kr/optimize?src=https://static.codenary.co.kr/framework_logo/emberjs.png&w=150&q=90",
    companyCount: randInt(1, 1000),
    stackCategoryId: 1,
  },
  {
    stackId: 18,
    name: "React",
    imageUrl:
      "https://image.wanted.co.kr/optimize?src=https://static.codenary.co.kr/framework_logo/reactjs.png&w=150&q=90",
    companyCount: randInt(1, 1000),
    stackCategoryId: 1,
  },
  {
    stackId: 19,
    name: "Backbone",
    imageUrl:
      "https://image.wanted.co.kr/optimize?src=https://static.codenary.co.kr/framework_logo/backbonejs.png&w=150&q=90",
    companyCount: randInt(1, 1000),
    stackCategoryId: 1,
  },
  {
    stackId: 20,
    name: "Fiber",
    imageUrl:
      "https://image.wanted.co.kr/optimize?src=https://static.codenary.co.kr/framework_logo/fiber.png&w=150&q=90",
    companyCount: randInt(1, 1000),
    stackCategoryId: 2,
  },
  {
    stackId: 21,
    name: "Apollo",
    imageUrl:
      "https://image.wanted.co.kr/optimize?src=https://static.codenary.co.kr/framework_logo/apollo.png&w=150&q=90",
    companyCount: randInt(1, 1000),
    stackCategoryId: 1,
  },
  {
    stackId: 22,
    name: "Playwright",
    imageUrl:
      "https://image.wanted.co.kr/optimize?src=https://static.codenary.co.kr/framework_logo/playwright.png&w=150&q=90",
    companyCount: randInt(1, 1000),
    stackCategoryId: 1,
  },
  {
    stackId: 23,
    name: "Rust",
    imageUrl:
      "https://image.wanted.co.kr/optimize?src=https://static.codenary.co.kr/framework_logo/rust.png&w=150&q=90",
    companyCount: randInt(1, 1000),
    stackCategoryId: 5,
  },
  {
    stackId: 24,
    name: "Go",
    imageUrl:
      "https://image.wanted.co.kr/optimize?src=https://static.codenary.co.kr/framework_logo/go.png&w=150&q=90",
    companyCount: randInt(1, 1000),
    stackCategoryId: 5,
  },
  {
    stackId: 25,
    name: "Elixir",
    imageUrl:
      "https://image.wanted.co.kr/optimize?src=https://static.codenary.co.kr/framework_logo/elixir.png&w=150&q=90",
    companyCount: randInt(1, 1000),
    stackCategoryId: 5,
  },
  {
    stackId: 26,
    name: "ReScript",
    imageUrl:
      "https://image.wanted.co.kr/optimize?src=https://static.codenary.co.kr/framework_logo/rescript.png&w=150&q=90",
    companyCount: randInt(1, 1000),
    stackCategoryId: 5,
  },
  {
    stackId: 27,
    name: "Elm",
    imageUrl: "https://avatars.githubusercontent.com/u/20698192?s=200&v=4",
    companyCount: randInt(1, 1000),
    stackCategoryId: 5,
  },
  {
    stackId: 28,
    name: "PHP",
    imageUrl:
      "https://image.wanted.co.kr/optimize?src=https://static.codenary.co.kr/framework_logo/php.png&w=150&q=90",
    companyCount: randInt(1, 1000),
    stackCategoryId: 5,
  },
  {
    stackId: 29,
    name: "Perl",
    imageUrl:
      "https://image.wanted.co.kr/optimize?src=https://static.codenary.co.kr/framework_logo/perl.png&w=150&q=90",
    companyCount: randInt(1, 1000),
    stackCategoryId: 5,
  },
  {
    stackId: 30,
    name: "Scala",
    imageUrl:
      "https://image.wanted.co.kr/optimize?src=https://static.codenary.co.kr/framework_logo/scala.png&w=150&q=90",
    companyCount: randInt(1, 1000),
    stackCategoryId: 5,
  },
];
export function randUrl() {
  return faker.internet.url();
}

export function randStack(): PersistStack {
  return STACKS[randInt(0, STACKS.length - 1)]!;
}

export function randStackId() {
  return randStack().stackId;
}

export function randStackIds(count: number = 3): number[] {
  const set = new Set<number>();
  while (set.size < count) {
    set.add(randStackId());
  }
  return Array.from(set);
}
