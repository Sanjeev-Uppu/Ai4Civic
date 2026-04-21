/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{ts,tsx}",
  ],
  theme: {
    extend: {
  colors: {
    civicBlue: "#2563eb",
    civicGreen: "#22c55e",
    neonPurple: "#a855f7",
    neonPink: "#ec4899",
  },
},
  },
  plugins: [],
};
