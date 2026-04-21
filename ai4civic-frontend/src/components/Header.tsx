import { Link } from "react-router-dom";
import logo from "/logo.png";

const Header = () => {
  return (
    <header
      className="
        h-16 px-6
        flex items-center justify-between
        bg-[#020b2e]/80
        backdrop-blur-xl
        border-b border-white/10
        shadow-lg
      "
    >
      {/* Logo Section */}
      <div className="flex items-center gap-3 group cursor-pointer">
        <div
          className="
            p-2 rounded-xl
            bg-white/10
            border border-white/20
            shadow-md
            transition-all duration-300
            group-hover:scale-110
            group-hover:shadow-[0_0_25px_rgba(56,189,248,0.6)]
          "
        >
          <img
            src={logo}
            alt="Ai4Civic"
            className="h-8 w-8 object-contain"
          />
        </div>

        <span
          className="
            font-extrabold text-lg
            bg-gradient-to-r from-sky-400 via-pink-400 to-yellow-300
            bg-clip-text text-transparent
            tracking-wide
            transition-all duration-300
            group-hover:drop-shadow-[0_0_12px_rgba(236,72,153,0.8)]
          "
        >
          Ai4Civic
        </span>
      </div>

      {/* Action Link */}
      <Link
        to="/submit"
        className="
          px-5 py-2 rounded-xl
          text-sm font-semibold
          text-black
          bg-gradient-to-r from-sky-400 via-pink-400 to-yellow-300
          shadow-md
          transition-all duration-300
          hover:scale-105
          hover:shadow-[0_0_25px_rgba(251,191,36,0.7)]
          active:scale-95
        "
      >
        AI for Every Citizen
      </Link>
    </header>
  );
};

export default Header;
