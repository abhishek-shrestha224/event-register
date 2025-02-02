import { UUID } from "crypto";
import { validate } from "uuid";

export function validateRegisterForm(formData: {
    registrationType: string;
    photo: File;
}): {
    pass: boolean;
    at: string | null;
    message: string | null;
} {
    const required = ["ORGANIZER", "VOLUNTEER", "SPEAKER", "VIP", "ATTENDEE"];

    if (!required.includes(formData.registrationType)) {
        console.log("invalid reg type");
        return {
            pass: false,
            at: "registrationType",
            message: "Invalid Registration Type",
        };
    }

    const photoFile = Array.isArray(formData.photo)
        ? formData.photo[0]
        : formData.photo;

    if (!photoFile) {
        console.log("photo not found");
        return {
            pass: false,
            at: "photo",
            message: "Photo is required",
        };
    }

    const filePath = photoFile.name;
    const fileName = filePath.split(".").shift();
    const extension = filePath.split(".").pop();
    console.log(filePath);
    console.log(fileName);
    console.log(extension);

    const maxFileSize = 2 * 1024 * 1024;
    if (photoFile.size > maxFileSize) {
        console.log("file too large");
        return {
            pass: false,
            at: "photo",
            message: "File must be less than 2MB",
        };
    }

    const validExtensions = ["png", "jpg", "jpeg"];
    // Get file extension and convert to lowercase

    if (!validExtensions.includes(extension.toLocaleLowerCase())) {
        console.log("invalid extension");
        return {
            pass: false,
            at: "photo",
            message: "File must be PNG, JPG, or JPEG.",
        };
    }
    return {
        pass: true,
        at: null,
        message: null,
    };
}

export function validateUuid(id: string): UUID {
    if (!id || id.trim() === "") {
        throw new Error("Invalid ID: ID cannot be null, blank, or null.");
    }
    if (!validate(id)) {
        throw new Error("Invalid ID: Not a valid UUID format.");
    }

    const uuid: UUID = id as UUID;

    return uuid;
}
