"use client";

import React, { useState, useEffect, use } from "react";
import { BadgePdfResponse } from "@/lib/dto"; // Adjust the path to your types
import { getBadgeById } from "@/utils/actions";
import PDFViewer from "@/components/PdfViewer";
import { useRouter } from "next/navigation";

export default function Page({
    params,
}: {
    params: Promise<{ slug: string }>; // Access slug directly from params
}) {
    const [badgeData, setBadgeData] = useState<BadgePdfResponse | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const router = useRouter(); // Using router for redirects

    const { slug } = use(params);

    useEffect(() => {
        const fetchBadgeData = async () => {
            try {
                const email = localStorage.getItem("email");

                if (!email) {
                    router.push("/"); // Redirect if email is not found
                    return; // Stop further execution
                }

                const response = await getBadgeById(slug, email);

                if (response.error) {
                    setError(response.error);
                    setIsLoading(false);
                } else if (response.data) {
                    setBadgeData(response.data);
                    setIsLoading(false);
                }
            } catch (err) {
                console.error("Error fetching badge data:", err);
                setError("Error fetching badge data");
                setIsLoading(false);
            }
        };

        fetchBadgeData();
    }, [slug, router]);

    if (isLoading) {
        return <div>Loading...</div>;
    }

    if (error) {
        return <div>{error}</div>;
    }

    if (!badgeData) {
        return <div>No badge data available</div>;
    }

    return (
        <div className="pt-16 w-full min-h-screen">
            <PDFViewer base64Pdf={badgeData.pdf} />
        </div>
    );
}
