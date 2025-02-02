import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
    title: "EventRegister::Home",
    description: "Register for Events - Get your pass.",
};

export default function RootLayout({
    children,
}: Readonly<{
    children: React.ReactNode;
}>) {
    return (
        <html lang="en">
            <body className="w-screen">{children}</body>
        </html>
    );
}
