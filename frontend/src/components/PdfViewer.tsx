// PDFViewer.tsx
"use client"; // Ensures the component is client-side only

import React, { useEffect, useState } from "react";

interface PDFViewerProps {
    base64Pdf: string; // Expecting base64 string here
}

const PDFViewer: React.FC<PDFViewerProps> = ({ base64Pdf }) => {
    const [pdfUrl, setPdfUrl] = useState<string | null>(null);

    useEffect(() => {
        // Convert the base64 string to a byte array
        const byteCharacters = atob(base64Pdf);
        const byteNumbers = new Array(byteCharacters.length);
        for (let i = 0; i < byteCharacters.length; i++) {
            byteNumbers[i] = byteCharacters.charCodeAt(i);
        }
        const byteArray = new Uint8Array(byteNumbers);

        // Create a Blob from the byte array and generate a URL for it
        const pdfBlob = new Blob([byteArray], { type: "application/pdf" });
        const url = URL.createObjectURL(pdfBlob);
        setPdfUrl(url);

        // Cleanup the URL after component unmounts
        return () => {
            if (pdfUrl) {
                URL.revokeObjectURL(pdfUrl);
            }
        };
    }, [base64Pdf]);

    if (!pdfUrl) {
        return <div>Loading PDF...</div>;
    }

    return (
        <iframe
            src={pdfUrl}
            title="Badge PDF"
            className="w-screen h-screen bg-white"
        />
    );
};

export default PDFViewer;
