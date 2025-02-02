"use client";

import { getBadgeById } from "./actions";

export async function openPdfInNewTab(id: string, email: string) {
    try {
        // Fetch the badge data using the slug and email
        const { data: badgeData, error } = await getBadgeById(id, email);

        if (error) {
            console.error("Error fetching badge data:", error);
            return;
        }

        if (!badgeData || !badgeData.pdf) {
            console.error("No PDF data found.");
            return;
        }

        // Convert the base64 PDF string to a byte array
        const byteCharacters = atob(badgeData.pdf);
        const byteNumbers = new Array(byteCharacters.length);
        for (let i = 0; i < byteCharacters.length; i++) {
            byteNumbers[i] = byteCharacters.charCodeAt(i);
        }
        const byteArray = new Uint8Array(byteNumbers);

        // Create a Blob from the byte array and generate a URL for it
        const pdfBlob = new Blob([byteArray], { type: "application/pdf" });
        const pdfUrl = URL.createObjectURL(pdfBlob);

        // Open the PDF in a new tab
        window.open(pdfUrl, "_blank");

        // Cleanup the URL after the tab is opened
        return () => {
            URL.revokeObjectURL(pdfUrl);
        };
    } catch (err) {
        console.error("Error:", err);
    }
}
