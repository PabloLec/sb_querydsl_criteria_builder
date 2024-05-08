## Setup

```bash
pnpm create vite@latest test-frontend -- --template vue-ts
cd test-frontend
pnpm install -D tailwindcss autoprefixer
pnpm i -D @types/node
npx shadcn-vue@latest init
npx shadcn-vue@latest add button
pnpm install
```