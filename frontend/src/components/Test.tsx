"use client";
import React from "react";
import { useForm } from "react-hook-form";

function Test() {
    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm();
    const onSubmit = (data: any) => console.log(data);
    return (
        <div>
            <form onSubmit={handleSubmit(onSubmit)}>
                <select
                    className="custom-select"
                    id="selectmethod"
                    defaultValue=""
                    {...register("exampleRequired", { required: true })}
                >
                    <option value="" disabled>
                        Select Option
                    </option>
                    <option value="1">Blue</option>
                    <option value="2">Red</option>
                </select>
                {errors.exampleRequired && (
                    <span className="formError errorMssg">
                        This field is required
                    </span>
                )}
                <br />
                <button type="submit">SUBMIT </button>
            </form>
        </div>
    );
}

export default Test;
