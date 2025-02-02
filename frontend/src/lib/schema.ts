import { z } from "zod";

export const SignUpFormSchema = z.object({
    firstName: z
        .string()
        .nonempty("First Name is required")
        .min(2, "First Name must be at least 2 characters long")
        .regex(/^[a-zA-Z]+$/, "First Name can only contain letters")
        .trim(),

    lastName: z
        .string()
        .nonempty("Last Name is required")
        .min(2, "Last Name must be at least 2 characters long")
        .regex(/^[a-zA-Z]+$/, "Last Name can only contain letters")
        .trim(),
    email: z
        .string()
        .email("Invalid email address")
        .nonempty("Email is required")
        .trim()
        .refine(
            (value) =>
                !/\b(?:tempmail|guerrillamail|mailinator)\.com\b/.test(value),
            {
                message: "Disposable email addresses are not allowed",
            }
        ),

    phoneNumber: z
        .string()
        .nonempty("Phone Number is required")
        .length(10, "Phone Number must be 10 digits")
        .refine((value) => /^9\d{9}$/.test(value), {
            message: "Phone number must have valid format",
        }),
});

export const LoginFormSchema = z.object({
    email: z
        .string()
        .email("Invalid email address")
        .nonempty("Email is required")
        .trim()
        .refine(
            (value) =>
                !/\b(?:tempmail|guerrillamail|mailinator)\.com\b/.test(value),
            {
                message: "Disposable email addresses are not allowed",
            }
        ),
});
