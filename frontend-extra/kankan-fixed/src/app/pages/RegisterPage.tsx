import { useState } from "react";
import { Link, useNavigate } from "react-router";
import logo from "../../assets/logo.png";
import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";
import { KeyRound, CheckCircle, XCircle } from "lucide-react";

function PasswordRule({ valid, text }: { valid: boolean; text: string }) {
  return (
    <div className="flex items-center gap-2 text-xs">
      {valid ? (
        <CheckCircle className="w-3.5 h-3.5 text-[#36B37E] flex-shrink-0" />
      ) : (
        <XCircle className="w-3.5 h-3.5 text-[#BFBFBF] flex-shrink-0" />
      )}
      <span className={valid ? "text-[#36B37E]" : "text-[#5E6C84]"}>{text}</span>
    </div>
  );
}

export function RegisterPage() {
  const navigate = useNavigate();
  const [invitationCode, setInvitationCode] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [passwordFocused, setPasswordFocused] = useState(false);

  const rules = {
    length: password.length >= 8 && password.length <= 30,
    digit: /[0-9]/.test(password),
    lower: /[a-z]/.test(password),
    upper: /[A-Z]/.test(password),
    special: /[@#$%^&+=!]/.test(password),
  };
  const passwordValid = Object.values(rules).every(Boolean);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!passwordValid) return;
    // Mock - in real app: POST /api/auth/register mit { invitationCode, userName, userLastName, userEmail, userPassword }
    navigate("/board");
  };

  return (
    <div className="min-h-screen bg-[#0052CC] flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        <div className="text-center mb-8">
          <img src={logo} alt="KanKan Logo" className="h-16 mx-auto mb-4" />
        </div>

        <div className="bg-white rounded-lg shadow-lg p-8">
          <h1 className="text-center mb-2 text-[#172B4D]">Konto erstellen</h1>
          <p className="text-center text-[#5E6C84] mb-6 text-sm">
            Sie benötigen einen Einladungscode Ihrer Organisation
          </p>

          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <Label htmlFor="invitationCode" className="text-[#172B4D]">
                Einladungscode
              </Label>
              <div className="relative mt-1">
                <KeyRound className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-[#5E6C84]" />
                <Input
                  id="invitationCode"
                  type="text"
                  placeholder="z.B. GOO-A3F2B"
                  value={invitationCode}
                  onChange={(e) => setInvitationCode(e.target.value.toUpperCase())}
                  className="pl-10 border-[#EBECF0] focus:border-[#0052CC] font-mono tracking-widest"
                  required
                />
              </div>
              <p className="text-xs text-[#5E6C84] mt-1">
                Den Code erhalten Sie von Ihrem Administrator.{" "}
                <Link to="/register/organization" className="text-[#0052CC] hover:underline">
                  Organisation registrieren?
                </Link>
              </p>
            </div>

            <div className="border-t border-[#EBECF0] pt-4">
              <div className="flex gap-3">
                <div className="flex-1">
                  <Label htmlFor="firstName" className="text-[#172B4D]">Vorname</Label>
                  <Input id="firstName" type="text" placeholder="Max" value={firstName}
                    onChange={(e) => setFirstName(e.target.value)}
                    className="mt-1 border-[#EBECF0] focus:border-[#0052CC]" required />
                </div>
                <div className="flex-1">
                  <Label htmlFor="lastName" className="text-[#172B4D]">Nachname</Label>
                  <Input id="lastName" type="text" placeholder="Mustermann" value={lastName}
                    onChange={(e) => setLastName(e.target.value)}
                    className="mt-1 border-[#EBECF0] focus:border-[#0052CC]" required />
                </div>
              </div>

              <div className="mt-4">
                <Label htmlFor="email" className="text-[#172B4D]">E-Mail</Label>
                <Input id="email" type="email" placeholder="ihre@email.de" value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="mt-1 border-[#EBECF0] focus:border-[#0052CC]" required />
              </div>

              <div className="mt-4">
                <Label htmlFor="password" className="text-[#172B4D]">Passwort</Label>
                <Input id="password" type="password" placeholder="••••••••" value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  onFocus={() => setPasswordFocused(true)}
                  className="mt-1 border-[#EBECF0] focus:border-[#0052CC]" required />
                {(passwordFocused || password.length > 0) && (
                  <div className="mt-2 p-3 bg-[#F4F5F7] rounded-md space-y-1">
                    <PasswordRule valid={rules.length} text="8–30 Zeichen" />
                    <PasswordRule valid={rules.upper} text="Mindestens ein Großbuchstabe (A–Z)" />
                    <PasswordRule valid={rules.lower} text="Mindestens ein Kleinbuchstabe (a–z)" />
                    <PasswordRule valid={rules.digit} text="Mindestens eine Zahl (0–9)" />
                    <PasswordRule valid={rules.special} text="Mindestens ein Sonderzeichen (@#$%^&+=!)" />
                  </div>
                )}
              </div>
            </div>

            <Button type="submit" disabled={!passwordValid}
              className="w-full bg-[#0052CC] text-white hover:bg-[#0747A6] disabled:opacity-50 disabled:cursor-not-allowed">
              Registrieren
            </Button>
          </form>

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
