"use client";

import React, { useState } from "react";
import { SubmitHandler, useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { SignUpFormSchema } from "@/lib/schema";
import { z } from "zod";
import { registerUser } from "@/utils/actions";

type Inputs = z.infer<typeof SignUpFormSchema>;
const SignUpForm = () => {
    const {
        register,
        handleSubmit,
        reset,
        formState: { errors },
    } = useForm<Inputs>({
        resolver: zodResolver(SignUpFormSchema),
    });

    const [isLoading, setIsLoading] = useState(false);

    const [isSuccess, setIsSuccess] = useState(false);

    const processForm: SubmitHandler<Inputs> = async (data) => {
        setIsLoading(true);
        const result = await registerUser(data);

        if (!result) {
            console.error("Something Went Wrong");
            return;
        }

        if (result.error) {
            console.error(result.error);
            return;
        }
        reset();
        console.log(data);

        setIsLoading(false);
        setIsSuccess(true);

        setTimeout(() => {
            setIsSuccess(false);
        }, 10000);
    };

    return (
        <div className="lg:w-1/3 w-full center-content flex-col gap-4 log:px-4 py-8">
            <h3 className="lg:text-3xl text-2xl font-bold">Sign Up Form</h3>

            <form
                className="lg:w-[90%] w-full mx-auto center-content flex-col"
                onSubmit={handleSubmit(processForm)}
            >
                <div className="mb-2 w-full lg:flex justify-between ">
                    <div className="lg:w-[48%] w-full">
                        <label
                            htmlFor="firstName"
                            className="block mb-2 text-sm font-medium text-dark"
                        >
                            First Name:
                        </label>
                        <input
                            type="text"
                            {...register("firstName")}
                            className="shadow-xs bg-light border border-accent text-dark text-sm rounded-lg focus:ring-accent focus:border-accent block w-full p-2.5 "
                            placeholder="John"
                            autoComplete="off"
                        />
                        <p className="mt-2 text-xs font-medium text-red-700">
                            <span className="mr-1">
                                {errors.firstName && "Error!"}
                            </span>
                            {errors.firstName?.message}
                        </p>
                    </div>

                    <div className="lg:w-[48%]">
                        <label
                            htmlFor="lastName"
                            className="block mb-2 text-sm font-medium text-dark"
                        >
                            Last Name:
                        </label>
                        <input
                            type="text"
                            {...register("lastName")}
                            className="shadow-xs bg-light border border-accent text-dark text-sm rounded-lg focus:ring-accent focus:border-accent block w-full p-2.5 "
                            placeholder="Doe"
                        />
                        <p className="mt-2 font-medium text-xs text-red-700">
                            <span className="mr-1">
                                {" "}
                                {errors.lastName && "Error!"}
                            </span>{" "}
                            {errors.lastName?.message}
                        </p>
                    </div>
                </div>
                <div className="mb-2 w-full">
                    <label
                        htmlFor="text"
                        className="block mb-2 text-sm font-medium text-dark"
                    >
                        Email:
                    </label>
                    <input
                        type="email"
                        {...register("email")}
                        className="shadow-xs bg-light border border-accent text-dark text-sm rounded-lg focus:ring-accent focus:border-accent block w-full p-2.5 "
                        placeholder="johndoe@email.com"
                    />
                    <p className="mt-2 font-medium text-xs text-red-700">
                        <span className="mr-1">
                            {" "}
                            {errors.email && "Error!"}
                        </span>{" "}
                        {errors.email?.message}
                    </p>
                </div>
                <div className="mb-2 w-full">
                    <label
                        htmlFor="phoneNumber"
                        className="block mb-2 text-sm font-medium text-dark"
                    >
                        Phone Number:
                    </label>
                    <input
                        type="numeric"
                        {...register("phoneNumber")}
                        className="shadow-xs bg-light border border-accent text-dark text-sm rounded-lg focus:ring-accent focus:border-accent block w-full p-2.5"
                        placeholder="9xxxxxxxxx"
                    />
                    <p className="mt-2 text-xs font-medium text-red-700">
                        <span className="mr-1">
                            {" "}
                            {errors.phoneNumber && "Error!"}
                        </span>{" "}
                        {errors.phoneNumber?.message}
                    </p>
                </div>

                <button
                    type="submit"
                    className="w-full font-semibold text-light bg-accent hover:opacity-90 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center"
                    disabled={isLoading}
                >
                    {isLoading ? "Loading..." : "SUBMIT"}
                </button>
            </form>

            <div className="h-4 text-sm font-bold text-accent flex gap-2">
                {isSuccess && (
                    <>
                        <p>Registration Successfull! Please Proceed to Login</p>
                        <button
                            onClick={() => setIsSuccess(false)}
                            type="button"
                            className=" top-3 end-2.5 text-accent bg-transparent hover:text-red-800 rounded-lg text-sm  ms-auto inline-flex justify-center items-center"
                            data-modal-hide="popup-modal"
                        >
                            <svg
                                className="w-3 h-3"
                                aria-hidden="true"
                                xmlns="http://www.w3.org/2000/svg"
                                fill="none"
                                viewBox="0 0 14 14"
                            >
                                <path
                                    stroke="currentColor"
                                    strokeLinecap="round"
                                    strokeLinejoin="round"
                                    strokeWidth="2"
                                    d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"
                                />
                            </svg>
                        </button>
                    </>
                )}
            </div>
        </div>
    );
};

export default SignUpForm;
