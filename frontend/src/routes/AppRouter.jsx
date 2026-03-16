import { Routes, Route } from "react-router-dom";

import LoginPage from "../pages/LoginPage";
import PrivateRoute from "../components/PrivateRoute";
import AppLayout from "../components/AppLayout";

import DashboardPage from "../pages/DashboardPage";
import PartyPage from "../pages/PartyPage";
import AddPartyPage from "../pages/AddPartyPage";
import LedgerPage from "../pages/LedgerPage";
import AddEntryPage from "../pages/AddEntryPage";
import AddAccountPage from "../pages/AddAccountPage";
import EditEntryPage from "../pages/EditEntryPage";
import PartiesPage from "../pages/PartiesPage";

function AppRouter() {
  return (
    <Routes>

      {/* Public route */}
      <Route path="/login" element={<LoginPage />} />

      {/* Protected routes */}
      <Route
        element={
          <PrivateRoute>
            <AppLayout />
          </PrivateRoute>
        }
      >

        <Route path="/" element={<DashboardPage />} />
        <Route path="/party/:id" element={<PartyPage />} />
        <Route path="/party/add" element={<AddPartyPage />} />
        <Route path="/account/:accountId" element={<LedgerPage />} />
        <Route path="/account/:accountId/add-entry" element={<AddEntryPage />} />
        <Route path="/party/:id/create-account" element={<AddAccountPage />} />
        <Route path="/ledger/edit/:entryId" element={<EditEntryPage />} />
        <Route path="/parties" element={<PartiesPage />} />

      </Route>

    </Routes>
  );
}

export default AppRouter;