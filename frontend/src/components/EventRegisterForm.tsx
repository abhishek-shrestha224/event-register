"use client";

import React, { useState } from "react";
import { Controller, SubmitHandler, useForm } from "react-hook-form";
import { registerToEvent } from "@/utils/actions";
import { redirect } from "next/navigation";
import { TfiClose } from "react-icons/tfi";

type Inputs = {
    registrationType: string;
    photo: File;
};

const EventRegistrationForm = ({ eventId }: { eventId: string }) => {
    const [errorMessage, setErrorMessage] = useState<string | undefined>(
        undefined
    );
    const {
        register,
        control,
        handleSubmit,
        setError,
        reset,
        setValue,
        formState: { errors },
    } = useForm<Inputs>();

    const [isLoading, setIsLoading] = useState(false);
    const [file, setFile] = useState<File | null>(null);

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files[0]) {
            const selectedFile = e.target.files[0];
            setFile(selectedFile);
            // Manually set the file in the form using RHF's setValue method
            setValue("photo", selectedFile);
        }
    };

    const onSubmit: SubmitHandler<Inputs> = async (formData) => {
        setIsLoading(true);

        console.log(formData.photo);
        const photoFile = Array.isArray(formData.photo)
            ? formData.photo[0]
            : formData.photo;

        if (formData.photo && photoFile.size > 2 * 1024 * 1024) {
            console.log("File too big");
            setError("photo", {
                type: "manual",
                message: "File too large. Max size is 2MB.",
            });
            setIsLoading(false);
            return; // Stop function execution
        }
        const email: string = localStorage.getItem("email") as string;

        const { data, at, message } = await registerToEvent(
            formData,
            eventId,
            email
        );
        console.log(at);
        console.log(message);
        if (at === "registrationType") {
            setError("registrationType", {
                type: "manual",
                message: message ?? "Invalid registration type",
            });
            setIsLoading(false);
            return;
        }

        if (at === "photo") {
            setError("photo", {
                type: "manual",
                message: message ?? "Invalid file",
            });
            setIsLoading(false);
            return;
        }

        if (!at && message) {
            setErrorMessage(message);
            setIsLoading(false);
            return;
        }

        if (!at) {
            reset();
            setIsLoading(false);
        }
        if (data) {
            redirect("/me");
        }
    };

    return (
        <div>
            <h3 className="lg:text-3xl font-bold">Registration Form</h3>
            <div className="lg:mt-4 h-4 text-sm font-bold text-accent flex justify-start items-center gap-4">
                {errorMessage && (
                    <>
                        <p className="text-red-800 font-medium text-lg">
                            Error Occured! {errorMessage}
                        </p>
                        <button
                            onClick={() => {
                                setErrorMessage(undefined);
                            }}
                            type="button"
                            className="text-red-700 bg-transparent hover:text-red-500 rounded-lg text-lg font-bold center-content"
                        >
                            <TfiClose fontSize={22} />
                        </button>
                    </>
                )}
            </div>
            <form
                onSubmit={handleSubmit(onSubmit)}
                className="center-content flex-col"
            >
                <div className="w-full lg:mt-8">
                    <label
                        htmlFor="registrationType"
                        className="block mb-2 text-sm font-medium text-dark"
                    >
                        First Name:
                    </label>
                    <select
                        id="registrationType"
                        {...register("registrationType")}
                        defaultValue="ATTENDEE"
                        className="shadow-xs bg-light border border-accent text-dark text-sm rounded-md focus:ring-accent focus:border-accent block w-full lg:p-2.5 p-1.5"
                    >
                        <option value="ATTENDEE">Attendee</option>
                        <option value="VIP">VIP</option>
                        <option value="SPEAKER">Speaker</option>
                        <option value="VOLUNTEER">Voliunteer</option>
                        <option value="ORGANIZER">Organizer</option>
                    </select>
                    <p className="lg:mt-2 text-xs font-medium text-red-700">
                        <span className="mr-1">
                            {errors.registrationType && "Error!"}
                        </span>
                        {errors.registrationType?.message}
                    </p>
                </div>

                <div className="w-full ">
                    <label
                        htmlFor="photo"
                        className="block mb-2 text-sm font-medium text-dark"
                    >
                        <Controller
                            name="photo"
                            control={control}
                            render={({
                                field: { value, onChange, ...field },
                            }) => {
                                return (
                                    <input
                                        className="block w-full text-sm text-dark cursor-pointer focus:outline-none"
                                        {...field}
                                        type="file"
                                        onChange={(event) => {
                                            handleFileChange(event);
                                        }}
                                    />
                                );
                            }}
                        />
                    </label>

                    <p className="mt-2 text-xs font-medium text-red-700">
                        <span className="mr-1">{errors.photo && "Error!"}</span>
                        {errors.photo?.message}
                    </p>
                </div>

                <button
                    type="submit"
                    className="lg:mt-8 w-full text-light bg-accent hover:opacity-90 focus:ring-4 focus:outline-none focus:ring-blue-300 font-semibold rounded-lg text-sm px-5 py-2.5 text-center"
                    disabled={isLoading}
                >
                    {isLoading ? "Loading..." : " REGISTER"}
                </button>
            </form>
        </div>
    );
};

export default EventRegistrationForm;
