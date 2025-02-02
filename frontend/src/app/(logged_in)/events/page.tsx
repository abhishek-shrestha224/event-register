import RegisterButton from "@/components/RegisterButton";
import { getAllEvents } from "@/utils/actions";

export default async function Page() {
    const { data, error } = await getAllEvents();

    return (
        <section className="pt-20 min-h-screen">
            <div className="lg:w-1/3 m-auto">
                <h2 className="font-bold font-heading lg:text-7xl text-5xl text-accent tracking-wide">
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
                <div className="mt-4 flex flex-col lg:gap-16 gap-8">
                    {data?.map((event) => (
                        <div
                            key={event.id}
                            className="text-left text-sm lg:text-md lg:px-4 px-2 lg:py-8 py-6 font-bold lg:text-lg border border-slate-300 rounded-md shadow-sm lg:shadow-xl"
                        >
                            <h5 className="font-semibold text-xl lg:text-2xl lg:mb-5 mb-2">
                                {event.name}
                            </h5>
                            <div className="flex font-medium lg:mb-3 mb-1">
                                <p className="font-medium w-36">Venue:</p>
                                <p>{event.venue}</p>
                            </div>
                            <div className="flex font-medium lg:mb-5 mb-2">
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
