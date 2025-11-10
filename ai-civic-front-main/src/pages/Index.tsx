import { useState } from "react";
import { ComplaintForm } from "@/components/ComplaintForm";
import { SuccessModal } from "@/components/SuccessModal";
import { FileText } from "lucide-react";

const Index = () => {
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [pdfPath, setPdfPath] = useState<string | undefined>();

  const handleSuccess = (path?: string) => {
    setPdfPath(path);
    setShowSuccessModal(true);
  };

  return (
    <div className="min-h-screen bg-gradient-civic">
      {/* Header */}
      <header className="bg-card/95 backdrop-blur-sm shadow-md sticky top-0 z-10">
        <div className="container mx-auto px-4 py-6">
          <div className="flex items-center gap-3">
            <div className="p-2 bg-gradient-civic rounded-lg">
              <FileText className="h-6 w-6 text-white" />
            </div>
            <h1 className="text-3xl font-bold bg-gradient-civic bg-clip-text text-transparent">
              Ai4Civic
            </h1>
          </div>
          <p className="text-muted-foreground mt-2 text-sm">
            Smart Complaint Management Portal
          </p>
        </div>
      </header>

      {/* Main Content */}
      <main className="container mx-auto px-4 py-12">
        <div className="max-w-3xl mx-auto">
          <div className="text-center mb-8">
            <h2 className="text-4xl font-bold text-white mb-4">
              Submit Your Complaint
            </h2>
            <p className="text-white/80 text-lg">
              Help us improve our services by reporting civic issues in your area
            </p>
          </div>

          <ComplaintForm onSuccess={handleSuccess} />
        </div>
      </main>

      {/* Success Modal */}
      <SuccessModal
        isOpen={showSuccessModal}
        onClose={() => setShowSuccessModal(false)}
        pdfPath={pdfPath}
      />
    </div>
  );
};

export default Index;
