import { useEffect, useState } from "react";
import StatCard from "../components/StatCard";
import AccountCard from "../components/AccountCard";
import api from "../services/api";

function DashboardPage() {

  const [accounts, setAccounts] = useState([]);
  const [stats, setStats] = useState({
    totalPending: 0,
    openAccounts: 0,
    closedAccounts: 0
  });

  const fetchDashboard = async () => {

    try {

      const res = await api.get("/account/pending?page=0&size=10");

      const page = res.data.data;

      const accountsData = page.content;

      setAccounts(accountsData);

      const totalPending = accountsData.reduce(
        (sum, a) => sum + Number(a.pendingAmount || 0),
        0
      );

      const openAccounts = accountsData.filter(
        a => a.status === "OPEN"
      ).length;

      const closedAccounts = accountsData.filter(
        a => a.status === "CLOSED"
      ).length;

      setStats({
        totalPending,
        openAccounts,
        closedAccounts
      });

    } catch (err) {

      console.error("Dashboard fetch failed", err);

    }

  };

  useEffect(() => {
    fetchDashboard();
  }, []);

  return (
    <div className="max-w-6xl mx-auto p-6">

      <h1 className="text-2xl font-bold mb-6">
        Dashboard
      </h1>

      {/* Stats */}
      <div className="grid grid-cols-4 gap-4 mb-8">

        <StatCard
          title="Total Pending"
          value={`₹${stats.totalPending.toLocaleString()}`}
          color="text-red-600"
        />

        <StatCard
          title="Open Accounts"
          value={stats.openAccounts}
          color="text-blue-600"
        />

        <StatCard
          title="Closed Accounts"
          value={stats.closedAccounts}
          color="text-green-600"
        />

        <StatCard
          title="Accounts"
          value={accounts.length}
          color="text-purple-600"
        />

      </div>

      {/* Accounts List */}
      <h1 className="text-2xl font-bold mb-6">
        Accounts :
      </h1>
      <div className="flex flex-col gap-4">

        {accounts.map(account => (
          <AccountCard
            key={account.id}
            account={account}
            refresh={fetchDashboard}
          />
        ))}

      </div>

    </div>
  );
}

export default DashboardPage;