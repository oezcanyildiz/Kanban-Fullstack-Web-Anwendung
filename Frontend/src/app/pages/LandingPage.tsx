import { useState, useEffect } from "react";
import { Link } from "react-router";
import logo from "../../assets/logo.png";
import { Github, GitBranch, Layers, Shield, Zap, ChevronLeft, ChevronRight, X, Terminal, Rocket, MessageSquare, RefreshCw } from "lucide-react";
import { Button } from "../components/ui/button";

export function LandingPage() {
  const screenshots = [
    { src: "/bilder/Board_Dash.png", alt: "Kanban Board Dashboard" },
    { src: "/bilder/Team_Leader_Dashboard.png", alt: "Team Leader Dashboard" },
    { src: "/bilder/Screenshot 2026-02-11 143909.png", alt: "Datenbank ERD Diagramm" },
    { src: "/bilder/Aufgabe_Update_Erstellung.png", alt: "Task Update und Erstellung" }
  ];

  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const [lightboxOpen, setLightboxOpen] = useState(false);
  const [step, setStep] = useState(0);

  useEffect(() => {
    const t1 = setTimeout(() => setStep(1), 500);
    const t2 = setTimeout(() => setStep(2), 1200);
    const t3 = setTimeout(() => setStep(3), 2500);
    return () => { clearTimeout(t1); clearTimeout(t2); clearTimeout(t3); };
  }, []);

  const nextImage = () => setCurrentImageIndex((prev) => (prev + 1) % screenshots.length);
  const prevImage = () => setCurrentImageIndex((prev) => (prev - 1 + screenshots.length) % screenshots.length);
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
        
        <h1 className="text-5xl md:text-6xl font-bold text-gray-900 mb-4 leading-tight">
          Teamarbeit organisieren.
        </h1>
        
        {/* Die animierte Zeile */}
        <h2 className="text-5xl md:text-6xl font-bold text-[#0052CC] mb-8 tracking-tight flex flex-wrap justify-center gap-4">
          <span className={step >= 1 ? "animate-shake inline-block" : "opacity-0"}>
            Einfach.
          </span>
          <span className={step >= 2 ? "animate-drop-earthquake inline-block" : "opacity-0"}>
            Sicher.
          </span>
          <span className={step >= 3 ? "animate-fade-in inline-block" : "opacity-0"}>
            Open Source.
          </span>
        </h2>

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
            {["Java 21", "Spring Boot", "Spring Security", "JWT", "Hibernate", "PostgreSQL", "Bucket4j", "Tailwind CSS", "Vibecoding", "Docker"].map(t => (
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

      {/* Screenshots Gallery Placeholder */}
      <section className="bg-gray-50 py-24 border-t border-gray-100 relative">
        <div className="container mx-auto px-6">
          <h2 className="text-3xl font-bold text-center text-gray-900 mb-4">Einblicke in die App</h2>
          <p className="text-center text-gray-500 mb-12 max-w-xl mx-auto">
            Klicke auf ein Bild, um es zu vergrößern.
          </p>
          
          <div className="relative max-w-5xl mx-auto group">
            {/* Carousel Image */}
            <div 
              className="rounded-2xl overflow-hidden shadow-2xl border border-gray-200 bg-white aspect-video cursor-pointer"
              onClick={() => setLightboxOpen(true)}
            >
              <img 
                src={screenshots[currentImageIndex].src} 
                alt={screenshots[currentImageIndex].alt} 
                className="w-full h-full object-cover transition-opacity duration-300" 
              />
            </div>
            
            {/* Carousel Controls */}
            <button 
              onClick={prevImage}
              className="absolute left-[-2rem] top-1/2 -translate-y-1/2 bg-white/90 hover:bg-white text-gray-800 p-3 rounded-full shadow-lg transition-transform hover:scale-110 z-10 hidden md:block border border-gray-100"
            >
              <ChevronLeft className="w-6 h-6" />
            </button>
            <button 
              onClick={nextImage}
              className="absolute right-[-2rem] top-1/2 -translate-y-1/2 bg-white/90 hover:bg-white text-gray-800 p-3 rounded-full shadow-lg transition-transform hover:scale-110 z-10 hidden md:block border border-gray-100"
            >
              <ChevronRight className="w-6 h-6" />
            </button>

            {/* Dots */}
            <div className="flex justify-center gap-2 mt-6">
              {screenshots.map((_, idx) => (
                <button
                  key={idx}
                  onClick={() => setCurrentImageIndex(idx)}
                  className={`w-3 h-3 rounded-full transition-colors ${idx === currentImageIndex ? 'bg-[#0052CC]' : 'bg-gray-300 hover:bg-gray-400'}`}
                />
              ))}
            </div>
          </div>
        </div>

        {/* Lightbox Modal */}
        {lightboxOpen && (
          <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/90 p-4 backdrop-blur-sm">
            <button 
              onClick={() => setLightboxOpen(false)}
              className="absolute top-6 right-6 text-white/70 hover:text-white transition-colors"
            >
              <X className="w-8 h-8" />
            </button>
            
            <button onClick={prevImage} className="absolute left-4 md:left-12 text-white/50 hover:text-white p-2">
              <ChevronLeft className="w-10 h-10" />
            </button>
            
            <img 
              src={screenshots[currentImageIndex].src} 
              alt={screenshots[currentImageIndex].alt} 
              className="max-w-full max-h-[90vh] object-contain rounded-lg shadow-2xl"
            />
            
            <button onClick={nextImage} className="absolute right-4 md:right-12 text-white/50 hover:text-white p-2">
              <ChevronRight className="w-10 h-10" />
            </button>
            
            <div className="absolute bottom-6 left-1/2 -translate-x-1/2 text-white/80 text-sm font-medium tracking-wide">
              {screenshots[currentImageIndex].alt}
            </div>
          </div>
        )}
      </section>

      {/* Demo Credentials */}
      <section className="bg-indigo-50 py-24 border-y border-indigo-100">
        <div className="container mx-auto px-6 text-center">
          <h2 className="text-3xl font-bold text-gray-900 mb-4">Live Demo testen</h2>
          <p className="text-gray-600 mb-10 max-w-2xl mx-auto">
            Logge dich mit diesen Test-Zugängen ein, um die verschiedenen Rollen und Funktionen direkt auszuprobieren.
          </p>
          
          <div className="grid md:grid-cols-3 gap-6 max-w-4xl mx-auto">
            {/* Admin Demo */}
            <div className="bg-white p-6 rounded-2xl shadow-sm border border-indigo-100 text-left">
              <div className="flex items-center gap-2 mb-4">
                <Shield className="w-5 h-5 text-purple-600" />
                <h3 className="font-bold text-gray-900">Admin Zugang</h3>
              </div>
              <p className="text-sm text-gray-500 mb-4">Zuständig für Mitarbeiterverwaltung und Einladungen.</p>
              <div className="bg-gray-50 p-3 rounded-lg border border-gray-100 space-y-2 text-sm">
                <div className="flex justify-between"><span className="text-gray-500">Email:</span><span className="font-semibold text-gray-900">stern@it.com</span></div>
                <div className="flex justify-between"><span className="text-gray-500">Passwort:</span><span className="font-semibold text-gray-900">Abc123456.!</span></div>
              </div>
            </div>

            {/* Leader Demo */}
            <div className="bg-white p-6 rounded-2xl shadow-sm border border-indigo-100 text-left">
              <div className="flex items-center gap-2 mb-4">
                <Layers className="w-5 h-5 text-blue-600" />
                <h3 className="font-bold text-gray-900">Leader Zugang</h3>
              </div>
              <p className="text-sm text-gray-500 mb-4">Erstellt Teams, Boards und weist Aufgaben zu.</p>
              <div className="bg-gray-50 p-3 rounded-lg border border-gray-100 space-y-2 text-sm">
                <div className="flex justify-between"><span className="text-gray-500">Email:</span><span className="font-semibold text-gray-900">mustermann@it.com</span></div>
                <div className="flex justify-between"><span className="text-gray-500">Passwort:</span><span className="font-semibold text-gray-900">Abc123456.!</span></div>
              </div>
            </div>

            {/* User Demo */}
            <div className="bg-white p-6 rounded-2xl shadow-sm border border-indigo-100 text-left">
              <div className="flex items-center gap-2 mb-4">
                <Zap className="w-5 h-5 text-emerald-600" />
                <h3 className="font-bold text-gray-900">User Zugang</h3>
              </div>
              <p className="text-sm text-gray-500 mb-4">Normaler Team-Mitarbeiter. Bearbeitet Tasks im Board.</p>
              <div className="bg-gray-50 p-3 rounded-lg border border-gray-100 space-y-2 text-sm">
                <div className="flex justify-between"><span className="text-gray-500">Email:</span><span className="font-semibold text-gray-900">yildiz@it.com</span></div>
                <div className="flex justify-between"><span className="text-gray-500">Passwort:</span><span className="font-semibold text-gray-900">Abc123456.!</span></div>
              </div>
            </div>
          </div>
          
          <div className="mt-10 mb-20">
            <Link to="/login">
              <Button size="lg" className="bg-[#0052CC] text-white hover:bg-[#0747A6] px-10 shadow-md">
                Zum Login
              </Button>
            </Link>
          </div>

          {/* Docker Quickstart Terminal */}
          <div className="max-w-3xl mx-auto bg-[#0a0a0a] rounded-2xl p-8 text-left shadow-2xl border border-gray-800 relative overflow-hidden">
            <div className="absolute top-0 right-0 p-8 opacity-5 pointer-events-none">
              <Terminal className="w-32 h-32 text-white" />
            </div>
            
            <div className="flex items-center gap-3 mb-3">
              <span className="flex gap-1.5">
                <span className="w-3 h-3 rounded-full bg-red-500"></span>
                <span className="w-3 h-3 rounded-full bg-yellow-500"></span>
                <span className="w-3 h-3 rounded-full bg-green-500"></span>
              </span>
              <h3 className="text-lg tracking-wide font-semibold text-gray-200 ml-2">Lokal ausführen mit Docker</h3>
            </div>
            
            <p className="text-gray-400 mb-6 text-sm max-w-xl">
              Möchtest du den Code inspizieren? Mit einem einzigen Befehl startest du die komplette Fullstack-Umgebung (Backend, Frontend & Datenbank) auf deinem PC.
            </p>
            
            <div className="bg-black/50 rounded-lg p-5 font-mono text-sm text-gray-300 border border-gray-800">
              <div className="text-emerald-400/80 mb-1"># 1. Repository klonen</div>
              <div className="mb-4">git clone https://github.com/oezcanyildiz/Kanban-Fullstack-Web-Anwendung</div>
              
              <div className="text-emerald-400/80 mb-1"># 2. Vollautomatisch starten</div>
              <div>docker-compose up --build -d</div>
            </div>
          </div>

        </div>
      </section>

      {/* V2 Roadmap */}
      <section className="py-24 bg-white border-t border-gray-100">
        <div className="container mx-auto px-6">
          <div className="max-w-3xl mx-auto text-center mb-16">
            <div className="inline-flex items-center gap-2 bg-purple-50 text-purple-700 text-sm px-4 py-1.5 rounded-full mb-4 font-medium">
              <Rocket className="w-4 h-4" /> Roadmap V2
            </div>
            <h2 className="text-3xl font-bold text-gray-900 mb-4">Die Zukunft von Boardly.one</h2>
            <p className="text-gray-500 text-lg">
              Dieses Projekt wird aktiv weiterentwickelt. Das nächste große Release fokussiert sich auf Echtzeit-Kommunikation.
            </p>
          </div>
          
          <div className="grid md:grid-cols-2 gap-8 max-w-5xl mx-auto">
            <div className="bg-purple-50 rounded-2xl p-8 border border-purple-100 flex gap-4">
              <div className="w-12 h-12 bg-purple-100 rounded-xl flex items-center justify-center flex-shrink-0">
                <MessageSquare className="w-6 h-6 text-purple-700" />
              </div>
              <div>
                <h3 className="text-xl font-bold text-gray-900 mb-2 text-left">Integrierter Team-Chat</h3>
                <p className="text-gray-600 leading-relaxed text-sm text-left">
                  Ein auf Spring WebSockets (STOMP) basierender Echtzeit-Chat, direkt in das Kanban-Board integriert. So können Teams ohne Verzögerung direkt über Aufgaben diskutieren und kommunizieren.
                </p>
              </div>
            </div>
            
            <div className="bg-blue-50 rounded-2xl p-8 border border-blue-100 flex gap-4">
              <div className="w-12 h-12 bg-blue-100 rounded-xl flex items-center justify-center flex-shrink-0">
                <RefreshCw className="w-6 h-6 text-blue-700" />
              </div>
              <div>
                <h3 className="text-xl font-bold text-gray-900 mb-2 text-left">Live Board-Synchronisation</h3>
                <p className="text-gray-600 leading-relaxed text-sm text-left">
                  Verschiebt ein Mitarbeiter ein Ticket, sehen es alle anderen im Bruchteil einer Sekunde – dank einer effizienten Message Broker Anbindung ohne lästiges manuelles Neuladen der Seite.
                </p>
              </div>
            </div>
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
              <Button size="lg" variant="outline" className="gap-2 px-8 hover:border-white text-gray-900">
                Über mich &amp; Lebenslauf
              </Button>
            </Link>
            <a href="https://github.com/oezcanyildiz" target="_blank">
              <Button size="lg" variant="outline" className="gap-2 px-8 hover:border-white text-gray-900">
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
