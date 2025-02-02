import { UUID } from "crypto";

export type Event = {
    id: UUID;
    name: string;
    venue: string;
    eventDate: string;
};

export type User = {
    firstName: string;
    lastName: string;
    email: string;
    phoneNumber: string;
    badges: Badge[];
};

export type RegistrationType =
    | "ORGANIZER"
    | "VOLUNTEER"
    | "SPEAKER"
    | "VIP"
    | "ATTENDEE";

export type Badge = {
    id: UUID;
    event: Event;
    userEmail: string;
    userFullName: string;
    registrationType: RegistrationType;
};

export type BadgePdfResponse = {
    badge: Badge;
    pdf: string;
};
