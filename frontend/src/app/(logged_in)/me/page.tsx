"use client";
import { User } from "@/lib/dto";
import { loginUser } from "@/utils/actions";
import { openPdfInNewTab } from "@/utils/client";

import { useRouter } from "next/navigation";
import React, { useEffect, useState } from "react";

const Page = () => {
    const [user, setUser] = useState<User | null>(null);

    const router = useRouter();
    useEffect(() => {
        (async () => {
            const email: string = localStorage.getItem("email") as string;
            if (!email) {
                router.push("/");
                return;
            }

            const { data, error } = await loginUser(email);
            if (error) {
                console.error("Login failed:", error);
                router.push("/");
            } else {
                console.log("User data:", data);
                setUser(data);
            }
        })();
    }, []);
    console.log(user);
    return (
        <section className="py-20 min-h-screen">
            <div className="lg:w-1/3 m-auto">
                <h2 className="font-bold font-heading lg:text-7xl text-5xl text-dark tracking-wide">
                    My Profile:
                </h2>
                {user && (
                    <>
                        <div className="lg:mt-12 mt-4 flex flex-col lg:gap-8 gap-3">
                            <div className="flex gap-12 items-end">
                                <p className="text-xs font-semibold w-28">
                                    First Name:
                                </p>
                                <span className="lg:text-lg font-semibold">
                                    {user.firstName}
                                </span>
                            </div>
                            <div className="flex gap-12 items-end">
                                <p className="text-xs font-semibold w-28">
                                    Last Name:
                                </p>
                                <span className="lg:text-lg font-semibold">
                                    {user.lastName}
                                </span>
                            </div>{" "}
                            <div className="flex gap-12 items-end">
                                <p className="text-xs font-semibold w-28">
                                    Email:{" "}
                                </p>
                                <span className="lg:text-lg font-semibold">
                                    {user.email}
                                </span>
                            </div>{" "}
                            <div className="flex gap-12 items-end">
                                <p className="text-xs font-semibold w-28">
                                    Phone:{" "}
                                </p>
                                <span className="lg:text-lg font-semibold">
                                    {user.phoneNumber}
                                </span>
                            </div>{" "}
                            <div className="flex gap-12 items-end">
                                <p className="text-xs font-semibold w-28">
                                    Badges Owned:{" "}
                                </p>
                                <span className="lg:text-lg font-semibold">
                                    {user.badges.length}
                                </span>
                            </div>
                        </div>
                        <div className="lg:mt-20 mt-10 flex flex-col gap-8">
                            <h5 className="font-bold text-3xl">My Badges:</h5>
                            {user.badges.map((badge) => (
                                <div
                                    key={badge.id}
                                    onClick={() => {
                                        const email =
                                            localStorage.getItem("email");
                                        if (email) {
                                            openPdfInNewTab(badge.id, email);
                                        } else {
                                            console.log(
                                                "Email not found. Unable to open PDF."
                                            );
                                        }
                                    }}
                                    className="text-left text-sm lg:text-md lg:px-4 px-2 lg:py-8 py-6 font-bold lg:text-lg border border-slate-300 lg:border-none rounded-md shadow-sm lg:shadow-xl flex flex-col gap-1 lg:gap-4 cursor-pointer"
                                >
                                    <h5 className="font-semibold lg:text-xl text-lg">
                                        {badge.event.name}
                                    </h5>
                                    <p className="font-medium lg:text-md">
                                        {badge.registrationType}
                                    </p>
                                    <p className="font-medium text-md">
                                        {badge.event.eventDate.replaceAll(
                                            "-",
                                            "."
                                        )}
                                    </p>
                                </div>
                            ))}
                        </div>
                    </>
                )}
            </div>
        </section>
    );
};

export default Page;
