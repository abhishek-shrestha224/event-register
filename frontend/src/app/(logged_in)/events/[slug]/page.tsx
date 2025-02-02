import { Suspense } from "react";
import EventRegisterForm from "@/components/EventRegisterForm";
import { getEventById } from "@/utils/actions";

export default async function Page({
    params,
}: {
    params: Promise<{ slug: string }>;
}) {
    const slug = (await params).slug;
    const { data: eventData, error } = await getEventById(slug);

    return (
        <section className="pt-20 min-h-screen">
            <div className="lg:w-1/3 m-auto">
                <h1 className="font-bold font-heading lg:text-7xl text-dark">
                    Register for Event
                </h1>

                {error && (
                    <div className="mt-4 h-4 text-sm font-bold text-accent flex justify-start items-center gap-4">
                        <p className="text-red-800 font-medium text-lg">
                            Error Occurred! {error}
                        </p>
                    </div>
                )}
                {eventData && (
                    <>
                        <main className="text-left py-8 font-bold lg:text-lg">
                            <h5 className="font-semibold text-2xl mb-5">
                                {eventData.name}
                            </h5>
                            <div className="flex font-medium mb-3">
                                <p className="font-medium w-36">Venue:</p>
                                <p>{eventData.venue}</p>
                            </div>
                            <div className="flex font-medium mb-5">
                                <p className="font-medium w-36">Event Date:</p>
                                <p>{eventData.eventDate}</p>
                            </div>
                        </main>

                        <Suspense fallback={<div>Loading form...</div>}>
                            <EventRegisterForm eventId={slug} />
                        </Suspense>
                    </>
                )}
            </div>
        </section>
    );
}
