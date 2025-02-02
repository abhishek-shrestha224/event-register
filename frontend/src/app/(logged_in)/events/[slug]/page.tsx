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
            <div className="lg:w-1/3 w-full m-auto">
                <h1 className="font-bold font-heading lg:text-7xl text-5xl text-dark">
                    Register for Event
                </h1>

                {error && (
                    <div className="lg:mt-4 mt-2 h-4 text-sm font-bold text-accent flex justify-start items-center gap-4">
                        <p className="text-red-800 font-medium text-lg">
                            Error Occurred! {error}
                        </p>
                    </div>
                )}
                {eventData && (
                    <>
                        <main className="text-left lg:py-8 py-4 font-bold lg:text-lg text-sm">
                            <h5 className="font-semibold lg:text-2xl text-xl lg:mb-5 mb-2">
                                {eventData.name}
                            </h5>
                            <div className="flex font-medium mb-3">
                                <p className="font-medium w-36">Venue:</p>
                                <p>{eventData.venue}</p>
                            </div>
                            <div className="flex font-medium lg:mb-5">
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
