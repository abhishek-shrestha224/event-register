"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import React from "react";

const Navbar = () => {
    const pathname = usePathname();
    return (
        <header className="w-full h-16 center-content fixed z-10 bg-light shadow-md">
            <nav className="lg:w-1/3 w-full flex justify-around font-bold lg:text-lg text-md text-accent">
                <Link
                    className={`${
                        pathname.startsWith("/events") && "border-b-2"
                    } hover:text-dark hover:border-black w-24 text-center border-accent`}
                    href="/events"
                >
                    Events
                </Link>
                <Link
                    className={`${
                        pathname.startsWith("/me") && "border-b-2"
                    } hover:text-dark hover:border-black w-24 text-center border-accent`}
                    href="/me"
                >
                    Profile
                </Link>
                <Link
                    className={`${
                        pathname === "/" && "border-b-2"
                    } hover:text-dark hover:border-black w-24 text-center border-accent`}
                    href="/"
                >
                    Logout
                </Link>
            </nav>
        </header>
    );
};

export default Navbar;
