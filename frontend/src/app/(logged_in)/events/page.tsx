import RegisterButton from "@/components/RegisterButton";
import { getAllEvents } from "@/utils/actions";

export default async function Page() {
    const { data, error } = await getAllEvents();

    return (
        <section className="pt-20 min-h-screen">
            <div className="lg:w-1/3 m-auto">
                <h2 className="font-bold font-heading lg:text-7xl text-accent tracking-wide">
                    Upcoming Events:
                </h2>

                {/* Error Handling */}
                {error && (
                    <div className="mt-4 h-4 text-sm font-bold text-accent flex justify-start items-center gap-4">
                        <p className="text-red-800 font-medium text-lg">
                            Error Occurred! {error}
                        </p>
                    </div>
                )}

                {/* Events List */}
                <div className="mt-4 flex flex-col gap-16">
                    {data?.map((event) => (
                        <div
                            key={event.id}
                            className="text-left px-4 py-8 font-bold lg:text-lg border border-slate-200 rounded-md shadow-xl"
                        >
                            <h5 className="font-semibold text-2xl mb-5">
                                {event.name}
                            </h5>
                            <div className="flex font-medium mb-3">
                                <p className="font-medium w-36">Venue:</p>
                                <p>{event.venue}</p>
                            </div>
                            <div className="flex font-medium mb-5">
                                <p className="font-medium w-36">Event Date:</p>
                                <p>{event.eventDate}</p>
                            </div>

                            {/* Client component for navigation */}
                            <RegisterButton eventId={event.id} />
                        </div>
                    ))}
                </div>
            </div>
        </section>
    );
}
