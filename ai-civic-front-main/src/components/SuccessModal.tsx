import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { CheckCircle, Download } from "lucide-react";

interface SuccessModalProps {
  isOpen: boolean;
  onClose: () => void;
  pdfPath?: string; // e.g. "S:\\AiCollege\\Ai4Civic\\backend\\letters\\complaint_17627.pdf"
}

export function SuccessModal({ isOpen, onClose, pdfPath }: SuccessModalProps) {
  const handleDownload = () => {
    if (!pdfPath) return;

    // Extract filename from Windows/Unix path
    const parts = pdfPath.split(/[/\\]/);
    const filename = parts[parts.length - 1];
    if (!filename) return;

    // Build the public URL that the backend serves
    const apiBase = import.meta.env.DEV ? "" : (import.meta.env.VITE_API_URL ?? "");
    const url = `${apiBase}/api/files/letter/${encodeURIComponent(filename)}`;

    window.open(url, "_blank");
  };

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent className="sm:max-w-md">
        <DialogHeader>
          <div className="flex justify-center mb-4">
            <div className="rounded-full bg-success/10 p-3">
              <CheckCircle className="h-12 w-12 text-success" />
            </div>
          </div>
          <DialogTitle className="text-center text-2xl">
            Complaint Successfully Registered!
          </DialogTitle>
          <DialogDescription className="text-center pt-2">
            Your complaint has been submitted and is now being processed. You'll receive updates via email.
          </DialogDescription>
        </DialogHeader>
        <div className="flex flex-col gap-3 pt-4">
          {pdfPath && (
            <Button
              onClick={handleDownload}
              className="w-full bg-gradient-civic hover:opacity-90 transition-opacity"
            >
              <Download className="mr-2 h-4 w-4" />
              Download PDF Receipt
            </Button>
          )}
          <Button variant="outline" onClick={onClose} className="w-full">
            Close
          </Button>
        </div>
      </DialogContent>
    </Dialog>
  );
}
