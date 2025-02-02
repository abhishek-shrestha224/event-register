"use client";
import { User } from "@/lib/dto";
import { loginUser } from "@/utils/actions";
import { redirect, useRouter } from "next/navigation";
import React, { useEffect, useState } from "react";

const Page = () => {
    const [user, setUser] = useState<User | null>(null);

    const router = useRouter();
    useEffect(() => {
        (async () => {
            const email: string = localStorage.getItem("email") as string;
            if (!email) {
                router.push("/"); // Redirect to the home page if email is not found
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
                <h2 className="font-bold font-heading lg:text-7xl text-dark tracking-wide">
                    My Profile:
                </h2>
                {user && (
                    <>
                        <div className="mt-20 50 flex flex-col gap-8">
                            <div className="flex gap-12 items-end">
                                <p className="lg:text-xs font-semibold w-28">
                                    First Name:
                                </p>
                                <span className="lg:text-md font-bold">
                                    {user.firstName}
                                </span>
                            </div>
                            <div className="flex gap-12 items-end">
                                <p className="lg:text-xs font-semibold w-28">
                                    Last Name:
                                </p>
                                <span className="lg:text-md font-bold">
                                    {user.lastName}
                                </span>
                            </div>{" "}
                            <div className="flex gap-12 items-end">
                                <p className="lg:text-xs font-semibold w-28">
                                    Email:{" "}
                                </p>
                                <span className="lg:text-md font-bold">
                                    {user.email}
                                </span>
                            </div>{" "}
                            <div className="flex gap-12 items-end">
                                <p className="lg:text-xs font-semibold w-28">
                                    Phone:{" "}
                                </p>
                                <span className="lg:text-md font-bold">
                                    {user.phoneNumber}
                                </span>
                            </div>{" "}
                            <div className="flex gap-12 items-end">
                                <p className="lg:text-xs font-semibold w-28">
                                    Badges Owned:{" "}
                                </p>
                                <span className="lg:text-md font-bold">
                                    {user.badges.length}
                                </span>
                            </div>
                        </div>
                        <div className="mt-20 50 flex flex-col gap-8">
                            <h5 className="font-bold text-3xl mb-">
                                My Badges:
                            </h5>
                            {user.badges.map((badge) => (
                                <div
                                    key={badge.id}
                                    onClick={() => {
                                        redirect(`me/${badge.id}`);
                                    }}
                                    className="text-left px-4 py-8 font-bold lg:text-lg border border-slate-200 rounded-md shadow-xl cursor-pointer flex flex-col gap-2"
                                >
                                    <h5 className="font-semibold text-xl">
                                        {badge.event.name}
                                    </h5>
                                    <p className="font-medium text-md">
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
