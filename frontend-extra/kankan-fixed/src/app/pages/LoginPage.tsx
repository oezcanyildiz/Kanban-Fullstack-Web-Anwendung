import { useState } from "react";
import { Link, useNavigate } from "react-router";
import logo from "../../assets/logo.png";
import { Button } from "../components/ui/button";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";

export function LoginPage() {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // Mock login - in real app, this would authenticate
    navigate("/board");
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
              className="w-full bg-[#0052CC] text-white hover:bg-[#0747A6]"
            >
              Anmelden
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
