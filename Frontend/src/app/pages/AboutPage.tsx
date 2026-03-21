import { Link } from "react-router";
import logo from "../../assets/logo.png";
import { Mail, Linkedin, Github, Download, MapPin, Award, Briefcase, GraduationCap, Code2 } from "lucide-react";
import { Button } from "../components/ui/button";

export function AboutPage() {
  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white border-b border-gray-100 sticky top-0 z-10">
        <div className="container mx-auto px-6 py-4 flex items-center justify-between">
          <Link to="/" className="flex items-center gap-2">
            <img src={logo} alt="Boardly" className="h-7" />
            <span className="font-bold text-gray-900">Boardly</span>
          </Link>
          <Link to="/login">
            <Button size="sm" className="bg-[#0052CC] text-white hover:bg-[#0747A6]">App öffnen</Button>
          </Link>
        </div>
      </header>

      <div className="container mx-auto px-6 py-16 max-w-4xl">

        {/* Profile Hero */}
        <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-8 mb-8 flex flex-col md:flex-row gap-8 items-start">
          <div className="w-24 h-24 rounded-2xl bg-[#0052CC] text-white text-3xl font-bold flex items-center justify-center flex-shrink-0">
            ÖY
          </div>
          <div className="flex-1">
            <div className="flex items-start justify-between flex-wrap gap-4">
              <div>
                <h1 className="text-2xl font-bold text-gray-900">Ozcan Yildiz</h1>
                <p className="text-[#0052CC] font-medium mt-1">Fachinformatiker für Anwendungsentwicklung</p>
                <div className="flex items-center gap-4 mt-3 text-sm text-gray-500 flex-wrap">
                  <span className="flex items-center gap-1"><MapPin className="w-3.5 h-3.5" /> Wiesbaden, Hessen</span>
                  <a href="mailto:oezcan.yildiz95@gmail.com" className="flex items-center gap-1 hover:text-[#0052CC] transition-colors">
                    <Mail className="w-3.5 h-3.5" /> oezcan.yildiz95@gmail.com
                  </a>
                </div>
              </div>
              <div className="flex gap-3">
                <a href="https://www.linkedin.com/in/oezcanyildiz" target="_blank">
                  <Button variant="outline" size="sm" className="gap-2"><Linkedin className="w-4 h-4" /> LinkedIn</Button>
                </a>
                <a href="https://github.com/oezcanyildiz" target="_blank">
                  <Button variant="outline" size="sm" className="gap-2"><Github className="w-4 h-4" /> GitHub</Button>
                </a>
              </div>
            </div>
            <p className="mt-4 text-gray-600 leading-relaxed text-sm">
              Junior Backend-Entwickler mit Spezialisierung auf das Java-Ecosystem. 9 Monate Praxiserfahrung
              an einem produktiven Lager-System bei Digital Titans GmbH. Boardly ist mein persönliches
              Fullstack-Projekt – gebaut um Enterprise-Patterns wie JWT, RBAC und saubere Schichtarchitektur
              in der Praxis anzuwenden.
            </p>
          </div>
        </div>

        {/* Open for work Banner */}
        <div className="bg-emerald-50 border border-emerald-200 rounded-xl p-5 mb-8 flex items-center justify-between flex-wrap gap-4">
          <div className="flex items-center gap-3">
            <div className="w-3 h-3 bg-emerald-500 rounded-full animate-pulse" />
            <div>
              <p className="font-semibold text-emerald-800">Offen für Jobangebote & Empfehlungen</p>
              <p className="text-emerald-600 text-sm">Ich freue mich über jede Nachricht – gerne direkt per E-Mail oder LinkedIn.</p>
            </div>
          </div>
          <a href="mailto:oezcan.yildiz95@gmail.com">
            <Button className="bg-emerald-600 hover:bg-emerald-700 text-white gap-2">
              <Mail className="w-4 h-4" /> Kontakt aufnehmen
            </Button>
          </a>
        </div>

        <div className="grid md:grid-cols-2 gap-6 mb-8">
          {/* Skills */}
          <div className="bg-white rounded-xl border border-gray-100 shadow-sm p-6">
            <h2 className="font-bold text-gray-900 mb-4 flex items-center gap-2"><Code2 className="w-5 h-5 text-[#0052CC]" /> Fähigkeiten</h2>
            <div className="space-y-3">
              {[
                { label: "Backend", value: "Java 21, Spring Boot, Spring Security, Hibernate/JPA" },
                { label: "Datenbanken", value: "PostgreSQL, relationales Design, Normalisierung (3NF)" },
                { label: "Frontend", value: "React, TypeScript, Tailwind CSS, HTML5, CSS3" },
                { label: "Testing", value: "JUnit 5, Mockito, Pytest" },
                { label: "DevOps", value: "Docker, Maven, Git" },
                { label: "Machine Learning", value: "scikit-learn, SVM, TF-IDF" },
              ].map(s => (
                <div key={s.label}>
                  <span className="text-xs font-semibold text-gray-400 uppercase tracking-wide">{s.label}</span>
                  <p className="text-sm text-gray-700">{s.value}</p>
                </div>
              ))}
            </div>
          </div>

          {/* Zertifikate & Bildung */}
          <div className="bg-white rounded-xl border border-gray-100 shadow-sm p-6">
            <h2 className="font-bold text-gray-900 mb-4 flex items-center gap-2"><Award className="w-5 h-5 text-[#0052CC]" /> Zertifikate</h2>
            <div className="space-y-3 mb-6">
              <div className="flex items-start gap-3">
                <div className="w-8 h-8 bg-orange-100 rounded-lg flex items-center justify-center flex-shrink-0 mt-0.5">
                  <span className="text-xs font-bold text-orange-600">OCA</span>
                </div>
                <div>
                  <p className="font-medium text-sm text-gray-900">Oracle Certified Associate: Java 8 SE</p>
                  <p className="text-xs text-gray-500">Oracle Corporation</p>
                </div>
              </div>
              <div className="flex items-start gap-3">
                <div className="w-8 h-8 bg-blue-100 rounded-lg flex items-center justify-center flex-shrink-0 mt-0.5">
                  <span className="text-xs font-bold text-blue-600">SCR</span>
                </div>
                <div>
                  <p className="font-medium text-sm text-gray-900">EXIN Agile Scrum Foundation</p>
                  <p className="text-xs text-gray-500">EXIN</p>
                </div>
              </div>
            </div>
            <h2 className="font-bold text-gray-900 mb-3 flex items-center gap-2"><GraduationCap className="w-5 h-5 text-[#0052CC]" /> Ausbildung</h2>
            <div className="space-y-2">
              <div>
                <p className="font-medium text-sm text-gray-900">Fachinformatiker Anwendungsentwicklung (IHK)</p>
                <p className="text-xs text-gray-500">Gfn GmbH · 2024–2026</p>
              </div>
              <div>
                <p className="font-medium text-sm text-gray-900">Staatlich geprüfter Webentwickler</p>
                <p className="text-xs text-gray-500">SGD Fernschule · 2022–2023</p>
              </div>
            </div>
          </div>
        </div>

        {/* Berufserfahrung */}
        <div className="bg-white rounded-xl border border-gray-100 shadow-sm p-6 mb-8">
          <h2 className="font-bold text-gray-900 mb-6 flex items-center gap-2"><Briefcase className="w-5 h-5 text-[#0052CC]" /> Berufserfahrung</h2>
          <div className="space-y-6">
            <div className="flex gap-4">
              <div className="flex flex-col items-center">
                <div className="w-3 h-3 bg-[#0052CC] rounded-full mt-1.5 flex-shrink-0" />
                <div className="w-0.5 bg-gray-200 flex-1 mt-2" />
              </div>
              <div className="pb-6">
                <div className="flex items-start justify-between flex-wrap gap-2">
                  <div>
                    <p className="font-semibold text-gray-900">Praktikant als Softwareentwickler</p>
                    <p className="text-sm text-[#0052CC]">Digital Titans GmbH</p>
                  </div>
                  <span className="text-xs text-gray-400 bg-gray-50 px-2 py-1 rounded">04.2025 – 01.2026</span>
                </div>
                <ul className="mt-3 space-y-1.5 text-sm text-gray-600">
                  <li className="flex gap-2"><span className="text-[#0052CC] mt-0.5">→</span> Backend-Optimierung eines produktiven Lager-Systems in PostgreSQL</li>
                  <li className="flex gap-2"><span className="text-[#0052CC] mt-0.5">→</span> RESTful APIs mit Spring Boot implementiert und erweitert</li>
                  <li className="flex gap-2"><span className="text-[#0052CC] mt-0.5">→</span> Zentralisiertes Exception-Handling und Unit-Tests (JUnit 5, Mockito)</li>
                  <li className="flex gap-2"><span className="text-[#0052CC] mt-0.5">→</span> IHK-Abschlussprojekt: ML-Pipeline mit 88% Genauigkeit (SVM, TF-IDF)</li>
                </ul>
              </div>
            </div>
            <div className="flex gap-4">
              <div className="flex flex-col items-center">
                <div className="w-3 h-3 bg-gray-300 rounded-full mt-1.5 flex-shrink-0" />
              </div>
              <div>
                <div className="flex items-start justify-between flex-wrap gap-2">
                  <div>
                    <p className="font-semibold text-gray-900">Technischer QA-Support</p>
                    <p className="text-sm text-gray-500">Opel Automobile Rüsselsheim</p>
                  </div>
                  <span className="text-xs text-gray-400 bg-gray-50 px-2 py-1 rounded">02.2022 – 12.2022</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Projekte */}
        <div className="bg-white rounded-xl border border-gray-100 shadow-sm p-6 mb-8">
          <h2 className="font-bold text-gray-900 mb-4 flex items-center gap-2"><Code2 className="w-5 h-5 text-[#0052CC]" /> Projekte</h2>
          <div className="space-y-4">
            <div className="border border-gray-100 rounded-xl p-5 hover:border-[#0052CC] transition-colors">
              <div className="flex items-start justify-between flex-wrap gap-3">
                <div>
                  <p className="font-semibold text-gray-900">Boardly – Kollaboratives Kanban-Board</p>
                  <p className="text-xs text-gray-500 mt-1">Fullstack · Java 21 · Spring Boot · React · PostgreSQL · Docker</p>
                </div>
                <a href="https://github.com/oezcanyildiz/Kanban-Fullstack-Web-Anwendung" target="_blank">
                  <Button variant="outline" size="sm" className="gap-1.5 text-xs"><Github className="w-3.5 h-3.5" /> Code</Button>
                </a>
              </div>
              <ul className="mt-3 space-y-1 text-sm text-gray-600">
                <li className="flex gap-2"><span className="text-[#0052CC] mt-0.5">→</span> Modulare 3-Tier-Architektur mit Interface-Pattern und Dependency Injection</li>
                <li className="flex gap-2"><span className="text-[#0052CC] mt-0.5">→</span> JWT-Authentifizierung und RBAC (Admin, Team-Owner, Mitglied)</li>
                <li className="flex gap-2"><span className="text-[#0052CC] mt-0.5">→</span> Organisations-Einladungssystem mit automatisch generiertem Code</li>
              </ul>
            </div>
          </div>
        </div>

        {/* Lebenslauf Download */}
        <div className="bg-[#0052CC] rounded-xl p-6 text-white text-center">
          <h2 className="font-bold text-xl mb-2">Vollständiger Lebenslauf</h2>
          <p className="text-blue-100 text-sm mb-6">
            Ich freue mich über Jobangebote, Empfehlungen oder einfach einen fachlichen Austausch.
          </p>
          <div className="flex items-center justify-center gap-4 flex-wrap">
            <a href="/Lebenslauf_Ozcan_Yildiz.pdf" download>
              <Button className="bg-white text-[#0052CC] hover:bg-gray-100 gap-2">
                <Download className="w-4 h-4" /> Lebenslauf herunterladen
              </Button>
            </a>
            <a href="mailto:oezcan.yildiz95@gmail.com">
              <Button className="bg-white text-[#0052CC] hover:bg-gray-100 gap-2">
                <Mail className="w-4 h-4" /> oezcan.yildiz95@gmail.com
              </Button>
            </a>
          </div>
        </div>

      </div>
    </div>
  );
}
