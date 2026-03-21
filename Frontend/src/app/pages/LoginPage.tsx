import { useState } from "react";
import { Link, useNavigate } from "react-router";
import logo from "../../assets/logo.png";
import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";
import { fetchApi } from "../services/api";

export function LoginPage() {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errorMsg, setErrorMsg] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setErrorMsg("");
    setLoading(true);

    try {
      const response = await fetchApi('/api/auth/login', {
        method: 'POST',
        body: JSON.stringify({
          userEmail: email,
          userPassword: password
        })
      });

      if (response) {
        if (response.jwtToken) localStorage.setItem('jwtToken', response.jwtToken);
        if (response.role) localStorage.setItem('userRole', response.role);
        if (response.organizationID) localStorage.setItem('organizationID', String(response.organizationID));
        if (response.userID) localStorage.setItem('userID', String(response.userID));
        if (response.userName) localStorage.setItem('userName', response.userName);
        if (response.userLastName) localStorage.setItem('userLastName', response.userLastName);
        if (response.userEmail) localStorage.setItem('userEmail', response.userEmail);
        if (response.organizationName) localStorage.setItem('organizationName', response.organizationName);
        if (response.invitationCode) localStorage.setItem('invitationCode', response.invitationCode);

        if (response.role === "ORG_ADMIN") {
          navigate("/admin");
        } else {
          navigate("/board");
        }
      }
    } catch (error: any) {
      setErrorMsg(error.message || "Anmeldung fehlgeschlagen");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-[#0052CC] flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        <div className="text-center mb-8">
          <img src={logo} alt="KanKan Logo" className="h-16 mx-auto mb-4" />
        </div>
        
        <div className="bg-white rounded-lg shadow-lg p-8">
          <h1 className="text-center mb-2 text-[#172B4D]">Willkommen zurück</h1>
          <p className="text-center text-[#5E6C84] mb-6 text-sm">
            Melden Sie sich an, um fortzufahren
          </p>

          {errorMsg && (
            <div className="mb-4 p-3 bg-red-50 text-red-600 text-sm rounded-md text-center">
              {errorMsg}
            </div>
          )}
          
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <Label htmlFor="email" className="text-[#172B4D]">
                E-Mail
              </Label>
              <Input
                id="email"
                type="email"
                placeholder="ihre@email.de"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="mt-1 border-[#EBECF0] focus:border-[#0052CC]"
                required
              />
            </div>
            
            <div>
              <Label htmlFor="password" className="text-[#172B4D]">
                Passwort
              </Label>
              <Input
                id="password"
                type="password"
                placeholder="••••••••"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="mt-1 border-[#EBECF0] focus:border-[#0052CC]"
                required
              />
            </div>
            
            <Button
              type="submit"
              disabled={loading}
              className="w-full bg-[#0052CC] text-white hover:bg-[#0747A6] disabled:opacity-50"
            >
              {loading ? "Wird angemeldet..." : "Anmelden"}
            </Button>
          </form>
          
          <div className="mt-6 text-center">
            <p className="text-sm text-[#5E6C84]">
              Noch kein Konto?{" "}
              <Link to="/register" className="text-[#0052CC] hover:underline">
                Registrieren
              </Link>
            </p>
          </div>
        </div>
        
        <div className="text-center mt-4">
          <Link to="/" className="text-white text-sm hover:underline">
            ← Zurück zur Startseite
          </Link>
        </div>
      </div>
    </div>
  );
}
