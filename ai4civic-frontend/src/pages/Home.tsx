import { Link } from "react-router-dom";

const Home = () => {
  return (
    <div className="
      min-h-screen
      bg-gradient-to-br from-[#020617] via-[#020b2e] to-[#03045e]
      relative overflow-hidden
    ">

      {/* Background glow accents */}
      <div className="absolute top-[-120px] left-[-120px] w-96 h-96 bg-sky-500/20 rounded-full blur-3xl" />
      <div className="absolute bottom-[-120px] right-[-120px] w-96 h-96 bg-purple-500/20 rounded-full blur-3xl" />

      <div className="relative z-10 max-w-7xl mx-auto px-8 py-24 grid md:grid-cols-2 gap-16 items-center">
        
        {/* LEFT CONTENT */}
        <div className="space-y-6">
          <h1 className="text-5xl font-extrabold leading-tight text-white">
            AI-Powered Civic <br />
            <span className="bg-gradient-to-r from-sky-400 via-pink-400 to-yellow-300 bg-clip-text text-transparent">
              Complaint System
            </span>
          </h1>

          <p className="text-lg text-slate-300 max-w-xl">
            Report civic issues smarter. Our AI routes complaints to the right
            authorities faster and tracks progress transparently.
          </p>

          <Link
            to="/submit"
            className="
              inline-block
              px-8 py-4 rounded-xl
              bg-gradient-to-r from-sky-400 via-pink-400 to-yellow-300
              text-black font-semibold
              shadow-lg shadow-yellow-400/30
              transition-all duration-300
              hover:scale-105
            "
          >
            Submit Complaint →
          </Link>
        </div>

        {/* RIGHT VISUAL */}
        <div className="relative flex justify-center">
          <div className="absolute inset-0 bg-gradient-to-tr from-sky-500/30 to-purple-500/30 blur-3xl rounded-full"></div>
          <img
            src="/logo.png"
            alt="Ai4Civic"
            className="relative rounded-3xl shadow-2xl bg-[#020617] p-8"
          />
        </div>

      </div>
    </div>
  );
};

export default Home;
