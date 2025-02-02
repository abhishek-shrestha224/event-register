import type { Config } from "tailwindcss";

export default {
    content: [
        "./src/pages/**/*.{js,ts,jsx,tsx,mdx}",
        "./src/components/**/*.{js,ts,jsx,tsx,mdx}",
        "./src/app/**/*.{js,ts,jsx,tsx,mdx}",
    ],
    theme: {
        extend: {
            fontFamily: {
                body: ["Poppins", "serif"],
                heading: ["Mouse Memoirs", "serif"],
            },
            colors: {
                dark: "#112825",
                light: "#e6f4f1",
                accent: "#005c54",
            },
        },
    },
    plugins: [],
} satisfies Config;
