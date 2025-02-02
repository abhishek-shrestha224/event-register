"use server";

import { z } from "zod";
import { LoginFormSchema, SignUpFormSchema } from "@/lib/schema";
import { User, Event, BadgePdfResponse, Badge } from "@/lib/dto";
import { validateRegisterForm, validateUuid } from "./validate";
import { redirect } from "next/navigation";

type UserCreate = z.infer<typeof SignUpFormSchema>;

type UserLogin = z.infer<typeof LoginFormSchema>;

export async function registerUser(
    data: UserCreate
): Promise<{ data: User | null; error: string | null }> {
    const result = SignUpFormSchema.safeParse(data);

    if (!result.success) {
        return { data: null, error: "Form Validation Failed!" };
    }
    try {
        const res = await fetch("http://localhost:8080/users", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(data),
        });

        if (res.status === 400) {
            throw new Error("404-Bad Request");
        }

        if (!res.ok) {
            throw new Error("500-Internal Server Error");
        }

        const userData = await res.json();
        return { data: userData, error: null };
    } catch (err) {
        if (err instanceof Error) {
            return { data: null, error: err.message };
        } else {
            return { data: null, error: "Something Went Wrong" };
        }
    }
}

export async function registerToEvent(
    formData: {
        registrationType: string;
        photo: File;
    },
    eventId: string,
    email: string
): Promise<{
    data: string | null;
    at: string | null;
    message: string | null;
}> {
    console.log(eventId);
    console.log(formData);
    try {
        const result = validateRegisterForm(formData);
        console.log(result);
        if (!result.pass) {
            return {
                data: null,
                at: result.at,
                message: result.message,
            };
        }

        const uuid = validateUuid(eventId);
        if (!email) {
            return {
                data: null,
                at: null,
                message: "User not found in session. Plese login",
            };
        }

        console.log(email);
        console.log("all ok");

        const formDataObj = new FormData();
        formDataObj.append("registrationType", formData.registrationType);
        formDataObj.append("photo", formData.photo);

        console.log(formDataObj);
        const res = await fetch(
            `http://localhost:8080/events/${uuid}/register`,
            {
                method: "POST",
                headers: {
                    "X-User-Email": email,
                },
                body: formDataObj,
            }
        );
        console.log(res);

        if (!res.ok) {
            console.log("error");
            throw new Error("Error on server");
        }

        const data: { id: string } = await res.json();
        return { data: data.id, at: null, message: null };
    } catch (err) {
        if (err instanceof Error) {
            return { data: null, at: null, message: err.message };
        } else {
            return { data: null, at: null, message: "No idea" };
        }
    }
}

export async function loginUser(
    email: string
): Promise<{ data: User | null; error: string | null }> {
    if (!email) {
        return { data: null, error: "Form Validation Failed!" };
    }

    try {
        const res = await fetch("http://localhost:8080/users/check", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "X-User-Email": email,
            },
        });

        if (res.status === 422) {
            throw new Error("422-Unprocessable Entity");
        }

        if (res.status === 404) {
            throw new Error("404-Not Found");
        }

        if (!res.ok) {
            throw new Error("500-Internal Server Error");
        }

        const userData = await res.json();
        return { data: userData, error: null };
    } catch (err) {
        if (err instanceof Error) {
            return { data: null, error: err.message };
        } else {
            return { data: null, error: "Something Went Wrong" };
        }
    }
}

export async function getAllEvents(): Promise<{
    error: null | string;
    data: Event[] | null;
}> {
    try {
        const res = await fetch("http://localhost:8080/events");

        if (!res.ok) {
            throw new Error("500-Internal Server Error");
        }

        const data = await res.json();
        console.log(data);
        return {
            error: null,
            data: data,
        };
    } catch (err) {
        if (err instanceof Error) {
            return { data: null, error: err.message };
        } else {
            return { data: null, error: "Something Went Wrong" };
        }
    }
}

export async function getEventById(id: string): Promise<{
    data: Event | null;
    error: null | string;
}> {
    try {
        const uuid = validateUuid(id);

        const res = await fetch(`http://localhost:8080/events/${uuid}`);

        if (!res.ok) {
            throw new Error("500-Internal Server Error");
        }

        const data = await res.json();
        console.log(data);
        return {
            data: data,
            error: null,
        };
    } catch (err) {
        if (err instanceof Error) {
            return { data: null, error: err.message };
        } else {
            return { data: null, error: "Something Went Wrong" };
        }
    }
}

export async function getBadgeById(
    id: string,
    email: string
): Promise<{
    data: BadgePdfResponse | null;
    error: null | string;
}> {
    try {
        const uuid = validateUuid(id);

        const res = await fetch(`http://localhost:8080/badges/${uuid}`, {
            method: "GET",
            headers: {
                "X-User-Email": email,
            },
        });

        if (res.status === 422) {
            throw new Error("422-Unprocessable Entity");
        }

        if (res.status === 404) {
            throw new Error("404-Not Found");
        }

        if (!res.ok) {
            throw new Error("500-Internal Server Error");
        }

        const data = await res.json();
        console.log(data);
        return {
            data: data,
            error: null,
        };
    } catch (err) {
        if (err instanceof Error) {
            return { data: null, error: err.message };
        } else {
            return { data: null, error: "Something Went Wrong" };
        }
    }
}
