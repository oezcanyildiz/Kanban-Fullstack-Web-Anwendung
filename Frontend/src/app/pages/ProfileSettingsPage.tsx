import { useState } from "react";
import { Link, useNavigate } from "react-router";
import { User, Users, Mail, Lock, ArrowLeft, CheckCircle, AlertCircle, Eye, EyeOff } from "lucide-react";
import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";
import { fetchApi } from "../services/api";

export function ProfileSettingsPage() {
  const navigate = useNavigate();

  const savedName = localStorage.getItem("userName") || "";
  const savedLastName = localStorage.getItem("userLastName") || "";
  const savedEmail = localStorage.getItem("userEmail") || "";

  const [email, setEmail] = useState(savedEmail);
  const [lastName, setLastName] = useState(savedLastName);
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [showOldPw, setShowOldPw] = useState(false);
  const [showNewPw, setShowNewPw] = useState(false);

  const [loading, setLoading] = useState(false);
  const [successMsg, setSuccessMsg] = useState<string | null>(null);
  const [errorMsg, setErrorMsg] = useState<string | null>(null);

  const initials = `${savedName.charAt(0)}${savedLastName.charAt(0)}`.toUpperCase() || "?";

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setSuccessMsg(null);
    setErrorMsg(null);

    const body: Record<string, string> = {
      userEmail: email,
      userLastName: lastName,
    };

    // Passwort nur mitschicken wenn der User es ausgefüllt hat
    if (newPassword) {
      body.oldPassword = oldPassword;
      body.userPassword = newPassword;
    }

    try {
      const response = await fetchApi("/api/user/update", {
        method: "PATCH",
        body: JSON.stringify(body),
      });

      // Aktualisierte Daten in localStorage speichern
      if (response.userLastName) localStorage.setItem("userLastName", response.userLastName);
      if (response.userEmail) localStorage.setItem("userEmail", response.userEmail);

      setLastName(response.userLastName || lastName);
      setEmail(response.userEmail || email);
      setOldPassword("");
      setNewPassword("");
      setSuccessMsg("Profil erfolgreich aktualisiert!");
    } catch (err: any) {
      setErrorMsg(err?.message || "Fehler beim Speichern. Bitte überprüfe deine Eingaben.");
    } finally {
      setLoading(false);
    }
  };

  const userRole = localStorage.getItem("userRole");
  const dashboardPath =
    userRole === "ORG_ADMIN" ? "/admin" :
    userRole === "TEAM_LEADER" ? "/leader" :
    "/board";

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col">
      {/* Header */}
      <header className="bg-white border-b border-gray-200 px-6 h-14 flex items-center justify-between sticky top-0 z-10 shadow-sm">
        <div className="flex items-center gap-3">
          <button
            onClick={() => navigate(-1)}
            className="p-2 rounded-lg hover:bg-gray-100 text-gray-500 hover:text-gray-800 transition-colors cursor-pointer"
          >
            <ArrowLeft className="w-4 h-4" />
          </button>
          <span className="font-semibold text-gray-800">Profileinstellungen</span>
        </div>
        <Link to={dashboardPath}>
          <Button variant="ghost" size="sm" className="text-gray-500 text-xs">
            Zurück zum Dashboard
          </Button>
        </Link>
      </header>

      <div className="flex-1 flex items-start justify-center px-4 py-10">
        <div className="w-full max-w-lg space-y-6">

          {/* Profile Avatar Card */}
          <div className="bg-white rounded-2xl border border-gray-100 shadow-sm p-6 flex items-center gap-5">
            <div className="w-16 h-16 rounded-2xl bg-[#0052CC] text-white text-xl font-bold flex items-center justify-center flex-shrink-0">
              {initials}
            </div>
            <div>
              <p className="font-semibold text-gray-900 text-lg">{savedName} {savedLastName}</p>
              <p className="text-sm text-gray-500">{savedEmail}</p>
              <span className="inline-block mt-1 text-xs bg-blue-50 text-blue-700 px-2 py-0.5 rounded-full font-medium capitalize">
                {(userRole || "USER").replace("_", " ").toLowerCase()}
              </span>
            </div>
          </div>

          {/* Invitation Code for Admin/Leader */}
          {(userRole === "ORG_ADMIN" || userRole === "TEAM_LEADER") && (
            <div className="bg-gradient-to-r from-blue-600 to-indigo-700 rounded-2xl p-6 text-white shadow-md">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-blue-100 text-xs font-semibold uppercase tracking-wider mb-1">Einladungscode für Mitarbeiter</p>
                  <code className="text-2xl font-bold tracking-widest">{localStorage.getItem("invitationCode") || "N/A"}</code>
                </div>
                <div className="bg-white/20 p-3 rounded-xl backdrop-blur-sm">
                  <Users className="w-6 h-6" />
                </div>
              </div>
              <p className="mt-4 text-sm text-blue-100/80">
                Teile diesen Code mit neuen Mitarbeitern, damit sie sich deiner Organisation {localStorage.getItem("organizationName")} anschließen können.
              </p>
            </div>
          )}

          {/* Feedback Messages */}
          {successMsg && (
            <div className="flex items-center gap-3 bg-emerald-50 border border-emerald-200 text-emerald-800 rounded-xl px-4 py-3">
              <CheckCircle className="w-5 h-5 text-emerald-600 flex-shrink-0" />
              <p className="text-sm font-medium">{successMsg}</p>
            </div>
          )}
          {errorMsg && (
            <div className="flex items-center gap-3 bg-red-50 border border-red-200 text-red-800 rounded-xl px-4 py-3">
              <AlertCircle className="w-5 h-5 text-red-500 flex-shrink-0" />
              <p className="text-sm font-medium">{errorMsg}</p>
            </div>
          )}

          {/* Form */}
          <form onSubmit={handleSave} className="space-y-5">

            {/* Persönliche Daten */}
            <div className="bg-white rounded-2xl border border-gray-100 shadow-sm p-6 space-y-4">
              <div className="flex items-center gap-2 mb-1">
                <User className="w-4 h-4 text-[#0052CC]" />
                <h2 className="font-semibold text-gray-900 text-sm">Persönliche Daten</h2>
              </div>

              <div className="space-y-1">
                <Label htmlFor="lastName" className="text-xs font-semibold text-gray-500 uppercase tracking-wide">
                  Nachname
                </Label>
                <Input
                  id="lastName"
                  value={lastName}
                  onChange={(e) => setLastName(e.target.value)}
                  placeholder="Dein Nachname"
                  required
                  className="h-10"
                />
              </div>
            </div>

            {/* E-Mail */}
            <div className="bg-white rounded-2xl border border-gray-100 shadow-sm p-6 space-y-4">
              <div className="flex items-center gap-2 mb-1">
                <Mail className="w-4 h-4 text-[#0052CC]" />
                <h2 className="font-semibold text-gray-900 text-sm">E-Mail Adresse</h2>
              </div>

              <div className="space-y-1">
                <Label htmlFor="email" className="text-xs font-semibold text-gray-500 uppercase tracking-wide">
                  E-Mail
                </Label>
                <Input
                  id="email"
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="deine@email.de"
                  required
                  className="h-10"
                />
              </div>
            </div>

            {/* Passwort */}
            <div className="bg-white rounded-2xl border border-gray-100 shadow-sm p-6 space-y-4">
              <div className="flex items-center gap-2 mb-1">
                <Lock className="w-4 h-4 text-[#0052CC]" />
                <h2 className="font-semibold text-gray-900 text-sm">Passwort ändern</h2>
                <span className="text-xs text-gray-400 font-normal ml-1">(optional)</span>
              </div>

              <div className="space-y-1">
                <Label htmlFor="oldPassword" className="text-xs font-semibold text-gray-500 uppercase tracking-wide">
                  Aktuelles Passwort
                </Label>
                <div className="relative">
                  <Input
                    id="oldPassword"
                    type={showOldPw ? "text" : "password"}
                    value={oldPassword}
                    onChange={(e) => setOldPassword(e.target.value)}
                    placeholder="••••••••"
                    className="h-10 pr-10"
                  />
                  <button
                    type="button"
                    onClick={() => setShowOldPw(v => !v)}
                    className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 cursor-pointer"
                  >
                    {showOldPw ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                  </button>
                </div>
              </div>

              <div className="space-y-1">
                <Label htmlFor="newPassword" className="text-xs font-semibold text-gray-500 uppercase tracking-wide">
                  Neues Passwort
                </Label>
                <div className="relative">
                  <Input
                    id="newPassword"
                    type={showNewPw ? "text" : "password"}
                    value={newPassword}
                    onChange={(e) => setNewPassword(e.target.value)}
                    placeholder="Min. 8 Zeichen, Groß-, Kleinbuchst., Ziffer & Sonderzeichen"
                    className="h-10 pr-10"
                  />
                  <button
                    type="button"
                    onClick={() => setShowNewPw(v => !v)}
                    className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600 cursor-pointer"
                  >
                    {showNewPw ? <EyeOff className="w-4 h-4" /> : <Eye className="w-4 h-4" />}
                  </button>
                </div>
                <p className="text-xs text-gray-400 mt-1">
                  Mindestens 8 Zeichen mit Großbuchstabe, Ziffer und Sonderzeichen (@#$%^&+=!)
                </p>
              </div>
            </div>

            {/* Save Button */}
            <Button
              type="submit"
              disabled={loading}
              className="w-full h-11 bg-[#0052CC] hover:bg-[#0747A6] text-white font-semibold text-base"
            >
              {loading ? "Wird gespeichert..." : "Änderungen speichern"}
            </Button>
          </form>
        </div>
      </div>
    </div>
  );
}
