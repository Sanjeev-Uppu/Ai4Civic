import { useRef, useState } from "react";
import { api } from "../services/api";

/* ---------------- UI Helpers ---------------- */

const Field = ({ label, action, children }: any) => (
  <div className="space-y-2">
    <div className="flex items-center justify-between">
      <label className="text-sm font-semibold text-sky-200 tracking-wide">
        {label}
      </label>
      {action}
    </div>
    {children}
  </div>
);

const inputStyle = `
  w-full rounded-xl
  border border-white/30
  px-4 py-3
  bg-white/10
  text-white
  placeholder:text-slate-300
  focus:outline-none
  focus:ring-2 focus:ring-sky-400/70
  transition-all duration-300
`;

/* ---------------- Component ---------------- */

const ComplaintForm = () => {
  const [loading, setLoading] = useState(false);

  const [form, setForm] = useState({
    name: "",
    email: "",
    location: "",
    category: "",
    priority: "Medium",
    description: "",
  });

  const [descriptionMode, setDescriptionMode] =
    useState<"manual" | "ai">("manual");

  const [locationMode, setLocationMode] =
    useState<"manual" | "gps">("manual");

  const [image, setImage] = useState<File | null>(null);
  const [preview, setPreview] = useState<string | null>(null);
  const [showCamera, setShowCamera] = useState(false);

  const videoRef = useRef<HTMLVideoElement>(null);
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const galleryInputRef = useRef<HTMLInputElement>(null);

  /* ---------------- Image ---------------- */

  const handleImageSelect = (file: File | null) => {
    if (!file) return;
    setImage(file);
    setPreview(URL.createObjectURL(file));
  };

  /* ---------------- Camera ---------------- */

  const startCamera = async () => {
    try {
      setShowCamera(true);
      const stream = await navigator.mediaDevices.getUserMedia({
        video: { facingMode: "environment" },
      });
      if (videoRef.current) videoRef.current.srcObject = stream;
    } catch {
      alert("Camera not available");
      setShowCamera(false);
    }
  };

  const stopCamera = () => {
    const video = videoRef.current;
    if (!video || !video.srcObject) return;
    const stream = video.srcObject as MediaStream;
    stream.getTracks().forEach((t) => t.stop());
    video.srcObject = null;
  };

  const capturePhoto = () => {
    const video = videoRef.current;
    const canvas = canvasRef.current;
    if (!video || !canvas) return;

    canvas.width = video.videoWidth;
    canvas.height = video.videoHeight;

    const ctx = canvas.getContext("2d");
    ctx?.drawImage(video, 0, 0);

    canvas.toBlob((blob) => {
      if (!blob) return;
      const file = new File([blob], "camera.jpg", { type: "image/jpeg" });
      handleImageSelect(file);
    });

    stopCamera();
    setShowCamera(false);
  };

  /* ---------------- GPS → Area Name ---------------- */

  const fetchLocationByGPS = () => {
    if (!navigator.geolocation) {
      alert("Geolocation not supported");
      return;
    }

    setLocationMode("gps");

    navigator.geolocation.getCurrentPosition(
      async (pos) => {
        const { latitude, longitude } = pos.coords;

        try {
          const res = await fetch(
            `https://nominatim.openstreetmap.org/reverse?format=json&lat=${latitude}&lon=${longitude}`
          );
          const data = await res.json();

          const addr = data.address || {};
          const area =
            addr.suburb ||
            addr.neighbourhood ||
            addr.village ||
            addr.town ||
            "";
          const mandal = addr.city_district || addr.county || "";
          const district = addr.state_district || addr.state || "";

          const finalLocation = [area, mandal, district]
            .filter(Boolean)
            .join(", ");

          setForm({
            ...form,
            location: finalLocation || "Location detected",
          });
        } catch {
          alert("Unable to fetch area name");
          setLocationMode("manual");
        }
      },
      () => {
        alert("Unable to fetch location");
        setLocationMode("manual");
      }
    );
  };

  /* ---------------- AI Description ---------------- */

  const generateByAI = () => {
    setDescriptionMode("ai");

    const loc = form.location || "the reported area";
    let text = "";

    switch (form.category) {
      case "Road":
        text = `An issue related to damaged or unsafe road conditions has been observed at ${loc}.
The situation is causing inconvenience and potential safety risks to the public.
Kindly take immediate action to resolve this issue.`;
        break;

      case "Water":
        text = `A water-related issue such as leakage or irregular supply has been identified at ${loc}.
This problem is affecting daily water usage for residents.
Kindly take immediate action to resolve this issue.`;
        break;

      case "Electricity":
        text = `Frequent electricity interruptions or infrastructure issues have been reported at ${loc}.
The disruption is affecting households and local businesses.
Kindly take immediate action to resolve this issue.`;
        break;

      case "Sanitation":
        text = `Sanitation and cleanliness issues have been noticed at ${loc}.
If left unattended, this may lead to health and environmental problems.
Kindly take immediate action to resolve this issue.`;
        break;

      default:
        text = `A civic issue has been reported at ${loc}.
The matter requires attention from the concerned authorities.
Kindly take immediate action to resolve this issue.`;
    }

    setForm({ ...form, description: text });
  };

  /* ---------------- Submit ---------------- */

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!image) {
      alert("Image proof is mandatory");
      return;
    }

    setLoading(true);

    try {
      const data = new FormData();
      data.append("complaint", JSON.stringify(form));
      data.append("image", image);

      await api.post("/complaints", data);
      alert("Complaint submitted successfully!");
    } catch {
      alert("Submission failed");
    } finally {
      setLoading(false);
    }
  };

  /* ---------------- Render ---------------- */

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-[#020617] via-[#020b2e] to-[#03045e] px-6">

      <div className="w-full max-w-xl bg-white/10 backdrop-blur-2xl border border-white/20 rounded-3xl p-10 shadow-2xl">

        <h2 className="text-3xl font-extrabold text-center text-white mb-6">
          Ai4Civic Complaint Portal
        </h2>

        <form onSubmit={handleSubmit} className="space-y-6">

          <Field label="Full Name">
            <input className={inputStyle} required
              value={form.name}
              onChange={(e) => setForm({ ...form, name: e.target.value })}
            />
          </Field>

          <Field label="Email">
            <input type="email" className={inputStyle} required
              value={form.email}
              onChange={(e) => setForm({ ...form, email: e.target.value })}
            />
          </Field>

          <Field
            label="Location"
            action={
              <div className="flex gap-2">
                <button type="button" onClick={fetchLocationByGPS}
                  className={`px-3 py-1 text-xs rounded-lg ${
                    locationMode === "gps"
                      ? "bg-green-400 text-black"
                      : "bg-white/20 text-white"
                  }`}>
                  📍 GPS
                </button>
                <button type="button" onClick={() => setLocationMode("manual")}
                  className={`px-3 py-1 text-xs rounded-lg ${
                    locationMode === "manual"
                      ? "bg-sky-400 text-black"
                      : "bg-white/20 text-white"
                  }`}>
                  ✍️ Manual
                </button>
              </div>
            }
          >
            <input
              className={`${inputStyle} ${locationMode === "gps" ? "opacity-80" : ""}`}
              disabled={locationMode === "gps"}
              value={form.location}
              onChange={(e) => setForm({ ...form, location: e.target.value })}
            />
          </Field>

          <div className="grid grid-cols-2 gap-4">
            <Field label="Category">
              <select className="w-full rounded-xl px-4 py-3 bg-[#0b122e] text-white"
                value={form.category}
                onChange={(e) => setForm({ ...form, category: e.target.value })}>
                <option value="" disabled>Select Category</option>
                <option>Road</option>
                <option>Water</option>
                <option>Electricity</option>
                <option>Sanitation</option>
              </select>
            </Field>

            <Field label="Priority">
              <select className="w-full rounded-xl px-4 py-3 bg-[#0b122e] text-white"
                value={form.priority}
                onChange={(e) => setForm({ ...form, priority: e.target.value })}>
                <option>Low</option>
                <option>Medium</option>
                <option>High</option>
              </select>
            </Field>
          </div>

          <Field
            label="Complaint Description"
            action={
              <div className="flex gap-2">
                <button type="button" onClick={generateByAI}
                  className={`px-3 py-1 text-xs rounded-lg ${
                    descriptionMode === "ai"
                      ? "bg-yellow-400 text-black"
                      : "bg-white/20 text-white"
                  }`}>
                  ✨ AI
                </button>
                <button type="button" onClick={() => setDescriptionMode("manual")}
                  className={`px-3 py-1 text-xs rounded-lg ${
                    descriptionMode === "manual"
                      ? "bg-sky-400 text-black"
                      : "bg-white/20 text-white"
                  }`}>
                  ✍️ Manual
                </button>
              </div>
            }
          >
            <textarea
              disabled={descriptionMode === "ai"}
              className={`${inputStyle} h-28 resize-none`}
              value={form.description}
              onChange={(e) =>
                setForm({ ...form, description: e.target.value })
              }
            />
          </Field>

          <Field label="Proof Image *">
            <input ref={galleryInputRef} type="file" accept="image/*"
              className="hidden"
              onChange={(e) => handleImageSelect(e.target.files?.[0] || null)} />

            <div className="flex gap-4">
              <button type="button" onClick={startCamera}
                className="flex-1 py-3 rounded-xl bg-green-400 text-black font-semibold">
                📸 Camera
              </button>
              <button type="button"
                onClick={() => galleryInputRef.current?.click()}
                className="flex-1 py-3 rounded-xl bg-sky-400 text-black font-semibold">
                🖼 Upload
              </button>
            </div>

            {showCamera && (
              <div className="mt-4 space-y-2">
                <video ref={videoRef} autoPlay className="rounded-xl" />
                <button type="button" onClick={capturePhoto}
                  className="w-full py-2 bg-yellow-400 rounded-xl font-bold">
                  Take Photo
                </button>
                <canvas ref={canvasRef} className="hidden" />
              </div>
            )}

            {preview && (
              <img src={preview}
                className="mt-4 w-full h-48 object-cover rounded-xl" />
            )}
          </Field>

          <button disabled={loading}
            className="w-full py-4 rounded-xl bg-gradient-to-r from-sky-400 via-pink-400 to-yellow-300 text-black font-bold">
            {loading ? "Submitting..." : "Submit Complaint"}
          </button>

        </form>
      </div>
    </div>
  );
};

export default ComplaintForm;
