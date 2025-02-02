import LoginForm from "@/components/LoginForm";
import SignUp from "@/components/SignUpForm";

export default function Home() {
    return (
        <section className="bg-light h-screen w-full center-content flex-col">
            <h1 className="lg:w-1/3 text-center font-bold font-heading lg:text-7xl text-dark">
                Event Register
            </h1>
            <SignUp />
            <LoginForm />
        </section>
    );
}
