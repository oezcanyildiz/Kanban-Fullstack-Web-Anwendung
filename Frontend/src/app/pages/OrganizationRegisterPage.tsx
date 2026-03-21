import { useState } from "react";
import { Link } from "react-router";
import logo from "../../assets/logo.png";
import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";
import { Building2, Copy, CheckCircle } from "lucide-react";
import { fetchApi } from "../services/api";

export function OrganizationRegisterPage() {
  const [orgName, setOrgName] = useState("");
  const [orgEmail, setOrgEmail] = useState("");
  const [invitationCode, setInvitationCode] = useState("");
  const [copied, setCopied] = useState(false);
  const [submitted, setSubmitted] = useState(false);
  const [errorMsg, setErrorMsg] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setErrorMsg("");
    setLoading(true);

    try {
      const response = await fetchApi('/api/organization/register', {
        method: 'POST',
        body: JSON.stringify({
          organizationName: orgName,
          organizationEmail: orgEmail
        })
      });

      // Das Backend sendet den generierten Invitation Code zurück
      if (response && response.invitationCode) {
        setInvitationCode(response.invitationCode);
        setSubmitted(true);
      } else {
        throw new Error("Kein Einladungscode vom Server erhalten");
      }
    } catch (error: any) {
      setErrorMsg(error.message || "Fehler beim Erstellen der Organisation");
    } finally {
      setLoading(false);
    }
  };

  const handleCopy = () => {
    navigator.clipboard.writeText(invitationCode);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div className="min-h-screen bg-[#0052CC] flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        <div className="text-center mb-8">
          <img src={logo} alt="KanKan Logo" className="h-16 mx-auto mb-4" />
        </div>

        <div className="bg-white rounded-lg shadow-lg p-8">
          {!submitted ? (
            <>
              <div className="flex justify-center mb-4">
                <div className="w-12 h-12 bg-[#E6F0FF] rounded-full flex items-center justify-center">
                  <Building2 className="w-6 h-6 text-[#0052CC]" />
                </div>
              </div>
              <h1 className="text-center mb-2 text-[#172B4D]">Organisation registrieren</h1>
              <p className="text-center text-[#5E6C84] mb-6 text-sm">
                Registrieren Sie Ihre Firma und laden Sie anschließend Ihre Mitarbeiter ein.
              </p>

              {errorMsg && (
                <div className="mb-4 p-3 bg-red-50 text-red-600 text-sm rounded-md text-center">
                  {errorMsg}
                </div>
              )}

              <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                  <Label htmlFor="orgName" className="text-[#172B4D]">Firmenname</Label>
                  <Input id="orgName" type="text" placeholder="Musterfirma GmbH" value={orgName}
                    onChange={(e) => setOrgName(e.target.value)}
                    className="mt-1 border-[#EBECF0] focus:border-[#0052CC]" required />
                </div>
                <div>
                  <Label htmlFor="orgEmail" className="text-[#172B4D]">Firmen-E-Mail</Label>
                  <Input id="orgEmail" type="email" placeholder="kontakt@musterfirma.de" value={orgEmail}
                    onChange={(e) => setOrgEmail(e.target.value)}
                    className="mt-1 border-[#EBECF0] focus:border-[#0052CC]" required />
                </div>
                <Button type="submit" disabled={loading} className="w-full bg-[#0052CC] text-white hover:bg-[#0747A6] disabled:opacity-50">
                  {loading ? "Wird erstellt..." : "Organisation erstellen"}
                </Button>
              </form>
            </>
          ) : (
            <>
              <div className="flex justify-center mb-4">
                <div className="w-12 h-12 bg-[#E3FCEF] rounded-full flex items-center justify-center">
                  <CheckCircle className="w-6 h-6 text-[#36B37E]" />
                </div>
              </div>
              <h1 className="text-center mb-2 text-[#172B4D]">Organisation erstellt!</h1>
              <p className="text-center text-[#5E6C84] mb-6 text-sm">
                Teilen Sie diesen Einladungscode mit Ihren Mitarbeitern, damit sie sich registrieren können.
              </p>

              <div className="bg-[#F4F5F7] rounded-lg p-4 mb-6">
                <p className="text-xs text-[#5E6C84] mb-2 text-center">Ihr Einladungscode</p>
                <div className="flex items-center gap-3">
                  <span className="flex-1 text-center text-2xl font-mono font-bold tracking-widest text-[#172B4D]">
                    {invitationCode}
                  </span>
                  <button onClick={handleCopy}
                    className="p-2 rounded-md hover:bg-[#EBECF0] transition-colors text-[#5E6C84]">
                    {copied
                      ? <CheckCircle className="w-5 h-5 text-[#36B37E]" />
                      : <Copy className="w-5 h-5" />}
                  </button>
                </div>
              </div>

              <div className="bg-[#FFFAE6] border border-[#FFD700] rounded-md p-3 mb-6">
                <p className="text-xs text-[#5E6C84]">
                  ⚠️ Speichern Sie diesen Code sicher. Er wird benötigt, damit Mitarbeiter sich registrieren können. Der erste Mitarbeiter der sich registriert wird automatisch zum Admin.
                </p>
              </div>

              <Link to="/register">
                <Button className="w-full bg-[#0052CC] text-white hover:bg-[#0747A6]">
                  Jetzt als erster Mitarbeiter registrieren
                </Button>
              </Link>
            </>
          )}

          <div className="mt-6 text-center">
            <p className="text-sm text-[#5E6C84]">
              Bereits ein Konto?{" "}
              <Link to="/login" className="text-[#0052CC] hover:underline">Anmelden</Link>
            </p>
          </div>
        </div>

        <div className="text-center mt-4">
          <Link to="/" className="text-white text-sm hover:underline">← Zurück zur Startseite</Link>
        </div>
      </div>
    </div>
  );
}
