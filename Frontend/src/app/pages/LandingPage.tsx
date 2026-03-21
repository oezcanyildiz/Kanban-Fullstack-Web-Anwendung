import { Link } from "react-router";
import logo from "../../assets/logo.png";
import { Github, GitBranch, Layers, Shield, Zap } from "lucide-react";
import { Button } from "../components/ui/button";

export function LandingPage() {
  return (
    <div className="min-h-screen bg-white">
      {/* Header */}
      <header className="border-b border-gray-100 sticky top-0 bg-white/95 backdrop-blur z-10">
        <div className="container mx-auto px-6 py-5 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <img src={logo} alt="Boardly" className="h-9" />
            <span className="font-bold text-gray-900 text-xl">Boardly</span>
            <span className="text-xs bg-emerald-100 text-emerald-700 px-2 py-0.5 rounded-full font-medium">Open Source</span>
          </div>
          <nav className="hidden md:flex items-center gap-8 text-sm text-gray-600">
            <a href="#features" className="hover:text-gray-900 transition-colors">Features</a>
            <a href="https://github.com/oezcanyildiz/Kanban-Fullstack-Web-Anwendung" target="_blank" className="hover:text-gray-900 transition-colors flex items-center gap-1">
              <Github className="w-4 h-4" /> GitHub
            </a>
            <Link to="/about" className="hover:text-gray-900 transition-colors">Über den Entwickler</Link>
          </nav>
          <div className="flex items-center gap-3">
            <Link to="/login">
              <Button variant="ghost" size="sm" className="text-gray-600">Login</Button>
            </Link>
            <Link to="/register/organization">
              <Button size="sm" className="bg-[#0052CC] text-white hover:bg-[#0747A6]">Jetzt starten</Button>
            </Link>
          </div>
        </div>
      </header>

      {/* Hero */}
      <section className="container mx-auto px-6 py-24 text-center">
        <div className="inline-flex items-center gap-2 bg-blue-50 text-blue-700 text-sm px-4 py-1.5 rounded-full mb-8 font-medium">
          <GitBranch className="w-4 h-4" />
          Aktiv in Entwicklung · v1.0
        </div>
        <h1 className="text-5xl font-bold text-gray-900 mb-6 leading-tight max-w-3xl mx-auto">
          Teamarbeit organisieren.<br />
          <span className="text-[#0052CC]">Einfach. Sicher. Open Source.</span>
        </h1>
        <p className="text-xl text-gray-500 mb-10 max-w-2xl mx-auto leading-relaxed">
          Boardly ist ein kollaboratives Kanban-Board mit echtem Organisations-Management,
          rollenbasierter Zugriffskontrolle und JWT-Authentifizierung.
        </p>
        <div className="flex items-center justify-center gap-4 flex-wrap">
          <Link to="/register/organization">
            <Button size="lg" className="bg-[#0052CC] text-white hover:bg-[#0747A6] px-8">
              Kostenlos starten
            </Button>
          </Link>
          <a href="https://github.com/oezcanyildiz/Kanban-Fullstack-Web-Anwendung" target="_blank">
            <Button size="lg" variant="outline" className="gap-2 px-8">
              <Github className="w-5 h-5" /> Quellcode ansehen
            </Button>
          </a>
        </div>
      </section>

      {/* Tech Stack Banner */}
      <section className="bg-gray-50 border-y border-gray-100 py-12">
        <div className="container mx-auto px-6 text-center">
          <p className="text-gray-500 text-sm mb-4 uppercase tracking-widest font-medium">Tech Stack</p>
          <div className="flex items-center justify-center gap-8 flex-wrap text-gray-700 font-semibold">
            {["Java 21", "Spring Boot", "Spring Security", "JWT", "PostgreSQL", "React", "TypeScript", "Docker"].map(t => (
              <span key={t} className="bg-white border border-gray-200 px-4 py-2 rounded-lg text-sm shadow-sm">{t}</span>
            ))}
          </div>
        </div>
      </section>

      {/* Features */}
      <section id="features" className="py-24">
        <div className="container mx-auto px-6">
          <h2 className="text-3xl font-bold text-center text-gray-900 mb-4">Was Boardly kann</h2>
          <p className="text-center text-gray-500 mb-16 max-w-xl mx-auto">
            Gebaut mit dem Fokus auf saubere Architektur und echte Enterprise-Patterns.
          </p>
          <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-6">
            <FeatureCard icon={<Shield className="w-6 h-6 text-[#0052CC]" />} title="RBAC & JWT" description="Rollenbasierte Zugriffskontrolle – Admin, Team-Owner und Mitglieder mit unterschiedlichen Rechten." />
            <FeatureCard icon={<Layers className="w-6 h-6 text-[#0052CC]" />} title="Organisations-System" description="Firmen registrieren sich, erhalten einen Einladungscode und laden Mitarbeiter ein." />
            <FeatureCard icon={<Zap className="w-6 h-6 text-[#0052CC]" />} title="WIP-Limits" description="Work-in-Progress-Limits pro Spalte verhindern Überlastung und fördern den Flow." />
            <FeatureCard icon={<Github className="w-6 h-6 text-[#0052CC]" />} title="Open Source" description="Vollständiger Quellcode auf GitHub. Offen für Feedback, Beiträge und Verbesserungen." />
          </div>
        </div>
      </section>

      {/* About / CTA */}
      <section className="bg-[#0052CC] py-20">
        <div className="container mx-auto px-6 text-center text-white">
          <h2 className="text-3xl font-bold mb-4">Ein Projekt von Ozcan Yildiz</h2>
          <p className="text-blue-100 mb-8 max-w-xl mx-auto">
            Boardly entstand als persönliches Lernprojekt mit dem Ziel, Enterprise-Patterns
            in einem echten Produkt anzuwenden. Der Code ist öffentlich und wird aktiv weiterentwickelt.
          </p>
          <div className="flex items-center justify-center gap-4 flex-wrap">
            <Link to="/about">
              <Button size="lg" variant="outline" className="gap-2 px-8 hover:border-white">
                Über mich &amp; Lebenslauf
              </Button>
            </Link>
            <a href="https://github.com/oezcanyildiz" target="_blank">
              <Button size="lg" variant="outline" className="gap-2 px-8 hover:border-white">
                <Github className="w-5 h-5" /> GitHub Profil
              </Button>
            </a>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="border-t border-gray-100 py-8">
        <div className="container mx-auto px-6 flex items-center justify-between flex-wrap gap-4">
          <div className="flex items-center gap-3">
            <img src={logo} alt="Boardly" className="h-6" />
            <span className="text-sm text-gray-500">
              Made with ♥ by{" "}
              <a href="https://www.linkedin.com/in/ozcanyildiz-de/" target="_blank" className="text-[#0052CC] hover:underline font-medium">
                Ozcan Yildiz
              </a>
            </span>
          </div>
          <div className="flex items-center gap-6 text-sm text-gray-400">
            <a href="https://github.com/oezcanyildiz/Kanban-Fullstack-Web-Anwendung" target="_blank" className="hover:text-gray-600 flex items-center gap-1">
              <Github className="w-4 h-4" /> Open Source
            </a>
            <Link to="/about" className="hover:text-gray-600">Kontakt</Link>
          </div>
        </div>
      </footer>
    </div>
  );
}

function FeatureCard({ icon, title, description }: { icon: React.ReactNode; title: string; description: string }) {
  return (
    <div className="bg-white border border-gray-100 rounded-xl p-6 shadow-sm hover:shadow-md transition-shadow">
      <div className="w-12 h-12 bg-blue-50 rounded-xl flex items-center justify-center mb-4">{icon}</div>
      <h3 className="font-semibold text-gray-900 mb-2">{title}</h3>
      <p className="text-gray-500 text-sm leading-relaxed">{description}</p>
    </div>
  );
}
