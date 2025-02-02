"use client";
import React, { useState } from "react";
import { TfiClose } from "react-icons/tfi";

type ErrorProps = {
    error: string | undefined;
};

const Error = ({ error }: ErrorProps) => {
    const [err, setErr] = useState<string | undefined>(error);
    return (
        <div className="mt-4 h-4 text-sm font-bold text-accent flex justify-start items-center gap-4">
            {err && (
                <>
                    <p className="text-red-800 font-medium text-lg">
                        Error Occured! {err}
                    </p>
                    <button
                        onClick={() => {
                            setErr(undefined);
                        }}
                        type="button"
                        className="text-red-700 bg-transparent hover:text-red-500 rounded-lg text-lg font-bold center-content"
                    >
                        <TfiClose fontSize={22} />
                    </button>
                </>
            )}
        </div>
    );
};

export default Error;
