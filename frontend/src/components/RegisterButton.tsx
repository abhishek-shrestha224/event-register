"use client";

import { useRouter } from "next/navigation";

export default function RegisterButton({ eventId }: { eventId: string }) {
    const router = useRouter();

    return (
        <button
            type="button"
            onClick={() => router.push(`/events/${eventId}`)}
            className="text-light bg-accent hover:opacity-90 focus:ring-4 focus:outline-none focus:ring-blue-300 font-semibold lg:rounded-md rounded-sm text-sm lg:px-5 px-3 lg:py-2.5 py-1.5 text-center"
        >
            REGISTER
        </button>
    );
}
